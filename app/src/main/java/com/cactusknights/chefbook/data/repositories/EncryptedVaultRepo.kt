package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.core.coroutines.CoroutineScopes
import com.cactusknights.chefbook.core.encryption.IHybridCryptor
import com.cactusknights.chefbook.data.IEncryptionSource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
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
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Singleton
class EncryptedVaultRepo @Inject constructor(
    @Local private val localSource: IEncryptionSource,
    @Remote private val remoteSource: IEncryptionSource,

    private val encryptionManager: IHybridCryptor,
    private val source: ISourceRepo,
    scopes: CoroutineScopes,
): IEncryptedVaultRepo {

    private var userKeys : KeyPair? = null
    private val vaultState : MutableStateFlow<EncryptedVaultState> = MutableStateFlow(EncryptedVaultState.DISABLED)

    init {
        scopes.repository.launch {
            getEncryptedVaultState()
        }
    }

    override suspend fun observeEncryptedVaultState(): StateFlow<EncryptedVaultState> = vaultState.asStateFlow()

    override suspend fun getEncryptedVaultState(): EncryptedVaultState {
        if (userKeys != null) return EncryptedVaultState.UNLOCKED
        val result = getKeyBySuitableSource()

        return if (result.isSuccess()) EncryptedVaultState.LOCKED else EncryptedVaultState.DISABLED
    }

    override suspend fun createEncryptedVault(password: String): SimpleAction {
        val ketPair = encryptionManager.generateKeyPair()
        val key = encryptionManager.generateSymmetricKey(password)
        val encryptedKeyPair = encryptionManager.encryptKeyPairBySymmetricKey(ketPair, key)

        val result = if (source.isOnlineMode()) {
            remoteSource.setUserKey(encryptedKeyPair)
        } else {
            localSource.setUserKey(encryptedKeyPair)
        }

        if (result.isSuccess() && source.isOnlineMode()) localSource.setUserKey(encryptedKeyPair)

        return result
    }

    override suspend fun unlockEncryptedVault(password: String): SimpleAction {
        val key = encryptionManager.generateSymmetricKey(password)
        val encryptedUserKeyResult = getKeyBySuitableSource()
        if (encryptedUserKeyResult.isFailure()) return encryptedUserKeyResult.asEmpty()

        return try {
            userKeys = encryptionManager.decryptKeyPairBySymmetricKey(encryptedUserKeyResult.data(), key)
            vaultState.emit(EncryptedVaultState.UNLOCKED)
            SuccessResult
        } catch (e: Exception) {
            Failure(AppError(AppErrorType.UNABLE_DECRYPT))
        }
    }

    override suspend fun lockEncryptedVault(): SimpleAction {
        userKeys = null
        vaultState.emit(EncryptedVaultState.LOCKED)
        return SuccessResult
    }

    override suspend fun getUserPublicKey(): ActionStatus<PublicKey> {
        val key = userKeys?.public ?: return Failure(AppError(AppErrorType.STORAGE_LOCKED))
        return DataResult(key)
    }

    override suspend fun getUserPrivateKey(): ActionStatus<PrivateKey> {
        val key = userKeys?.private ?: return Failure(AppError(AppErrorType.STORAGE_LOCKED))
        return DataResult(key)
    }

    override suspend fun deleteEncryptedVault(): SimpleAction {
        val result = if (source.useRemoteSource()) {
            remoteSource.deleteUserKey()
        } else {
            localSource.deleteUserKey()
        }

        if (result.isSuccess() && source.useRemoteSource()) localSource.deleteUserKey()
        return result
    }

    private suspend fun getKeyBySuitableSource(): ActionStatus<ByteArray> {
        var result = localSource.getUserKey()
        if (result.isFailure() && source.useRemoteSource()) {
            result = remoteSource.getUserKey()
            if (result.isSuccess()) {
                localSource.setUserKey(result.data())
            }
        }

        return result
    }
}