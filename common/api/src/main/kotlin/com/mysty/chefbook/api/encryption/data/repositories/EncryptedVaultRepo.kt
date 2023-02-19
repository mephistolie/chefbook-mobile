package com.mysty.chefbook.api.encryption.data.repositories

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType
import com.mysty.chefbook.api.common.communication.errors.ServerError
import com.mysty.chefbook.api.common.communication.errors.ServerErrorType
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.crypto.IHybridCryptor
import com.mysty.chefbook.api.encryption.data.ILocalEncryptionSource
import com.mysty.chefbook.api.encryption.data.IRemoteEncryptionSource
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import java.security.PrivateKey
import java.security.PublicKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class EncryptedVaultRepo(
    private val localSource: ILocalEncryptionSource,
    private val remoteSource: IRemoteEncryptionSource,

    private val encryptionManager: IHybridCryptor,
    private val source: ISourcesRepo,
    scopes: CoroutineScopes,
): IEncryptedVaultRepo {

    private val vaultState : MutableStateFlow<EncryptedVaultState?> = MutableStateFlow(null)

    init {
        scopes.repository.launch {
            vaultState.emit(getEncryptedVaultState())
        }
    }

    override fun observeEncryptedVaultState(): Flow<EncryptedVaultState?> = vaultState.asStateFlow()

    override suspend fun getEncryptedVaultState(): EncryptedVaultState {
        (vaultState.value as? EncryptedVaultState.Unlocked)?.let { state ->
            return EncryptedVaultState.Unlocked(state.keys)
        }
        val result = getKeyBySuitableSource()

        return if (result.isSuccess()) EncryptedVaultState.Locked else EncryptedVaultState.Disabled
    }

    override suspend fun refreshEncryptedVaultState() {
        getKeyBySuitableSource()
    }

    override suspend fun createEncryptedVault(password: String, salt: ByteArray): SimpleAction {
        val keyPair = encryptionManager.generateKeyPair()
        val key = encryptionManager.generateSymmetricKey(password, salt)
        val encryptedKeyPair = encryptionManager.encryptKeyPairBySymmetricKey(keyPair, key)

        val result = if (source.isOnlineMode()) {
            remoteSource.setUserKey(encryptedKeyPair)
        } else {
            localSource.setUserKey(encryptedKeyPair)
        }

        if (result.isSuccess() && source.isOnlineMode()) {
            localSource.setUserKey(encryptedKeyPair)
            vaultState.emit(EncryptedVaultState.Unlocked(keyPair))
        }

        return result
    }

    override suspend fun unlockEncryptedVault(password: String, salt: ByteArray): SimpleAction {
        val key = encryptionManager.generateSymmetricKey(password, salt)
        val encryptedUserKeyResult = getKeyBySuitableSource()
        if (encryptedUserKeyResult.isFailure()) return encryptedUserKeyResult.asEmpty()

        return try {
            val userKeys = encryptionManager.decryptKeyPairBySymmetricKey(encryptedUserKeyResult.data(), key)
            vaultState.emit(EncryptedVaultState.Unlocked(userKeys))
            SuccessResult
        } catch (e: Exception) {
            Failure(AppError(AppErrorType.UNABLE_DECRYPT))
        }
    }

    override suspend fun lockEncryptedVault(): SimpleAction {
        vaultState.emit(EncryptedVaultState.Locked)
        return SuccessResult
    }

    override suspend fun getUserPublicKey(): ActionStatus<PublicKey> =
        (vaultState.value as? EncryptedVaultState.Unlocked)?.let { state -> DataResult(state.keys.public) }
            ?: Failure(AppError(AppErrorType.ENCRYPTED_VAULT_LOCKED))

    override suspend fun getUserPrivateKey(): ActionStatus<PrivateKey> =
        (vaultState.value as? EncryptedVaultState.Unlocked)?.let { state -> DataResult(state.keys.private) }
            ?: Failure(AppError(AppErrorType.ENCRYPTED_VAULT_LOCKED))

    override suspend fun deleteEncryptedVault(): SimpleAction {
        val result = if (source.useRemoteSource()) {
            remoteSource.deleteUserKey()
        } else {
            localSource.deleteUserKey()
        }

        if (result.isSuccess() && source.useRemoteSource()) {
            localSource.deleteUserKey()
            vaultState.emit(EncryptedVaultState.Disabled)
        }
        return result
    }

    private suspend fun getKeyBySuitableSource(): ActionStatus<ByteArray> {
        var result = localSource.getUserKey()
        if (source.useRemoteSource()) {
            val remoteLinkResult = remoteSource.getUserKeyLink()
            when {
                result.isFailure() && remoteLinkResult.isSuccess() -> {
                    val remoteResult = remoteSource.getUserKey(remoteLinkResult.data())
                    if (remoteResult.isSuccess()) {
                        localSource.setUserKey(remoteResult.data())
                        result = remoteResult
                        vaultState.emit(EncryptedVaultState.Locked)
                    }
                }

                remoteLinkResult is Failure&& (remoteLinkResult.error is ServerError && remoteLinkResult.error.type == ServerErrorType.NOT_FOUND)
                        && result.isSuccess() -> {
                    localSource.deleteUserKey()
                    vaultState.emit(EncryptedVaultState.Disabled)
                }
            }
        }

        return result
    }
}