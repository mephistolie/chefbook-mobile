package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.core.encryption.IHybridCryptor
import com.cactusknights.chefbook.data.ILocalEncryptionSource
import com.cactusknights.chefbook.data.IRemoteEncryptionSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.ISourceRepo
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import java.security.PrivateKey
import java.security.PublicKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EncryptedVaultRepo(
    private val localSource: ILocalEncryptionSource,
    private val remoteSource: IRemoteEncryptionSource,

    private val encryptionManager: IHybridCryptor,
    private val source: ISourceRepo,
    scopes: CoroutineScopes,
): IEncryptedVaultRepo {

    private val vaultState : MutableStateFlow<EncryptedVaultState> = MutableStateFlow(EncryptedVaultState.Disabled)

    init {
        scopes.repository.launch {
            vaultState.emit(getEncryptedVaultState())
        }
    }

    override suspend fun observeEncryptedVaultState(): StateFlow<EncryptedVaultState> = vaultState.asStateFlow()

    override suspend fun getEncryptedVaultState(): EncryptedVaultState {
        (vaultState.value as? EncryptedVaultState.Unlocked)?.let { state ->
            return EncryptedVaultState.Unlocked(state.keys)
        }
        val result = getKeyBySuitableSource()

        return if (result.isSuccess()) EncryptedVaultState.Locked else EncryptedVaultState.Disabled
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
            ?: Failure(AppError(AppErrorType.STORAGE_LOCKED))

    override suspend fun getUserPrivateKey(): ActionStatus<PrivateKey> =
        (vaultState.value as? EncryptedVaultState.Unlocked)?.let { state -> DataResult(state.keys.private) }
            ?: Failure(AppError(AppErrorType.STORAGE_LOCKED))

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
                    }
                }

                remoteLinkResult.isFailure() && result.isSuccess() -> {
                    localSource.deleteUserKey()
                }
            }
        }

        return result
    }
}