package com.cactusknights.chefbook.repositories.sync

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.encryption.EncryptionManager
import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.domain.EncryptionRepository
import com.cactusknights.chefbook.repositories.local.datasources.LocalEncryptionDataSource
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteEncryptionDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.security.*
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncEncryptionRepository @Inject constructor(
    private val localSource: LocalEncryptionDataSource,
    private val remoteSource: RemoteEncryptionDataSource,
    private val encryptionManager: EncryptionManager,
    private val settings: SettingsManager
): EncryptionRepository {

    private var userPublicKey : PublicKey? = null
    private var userPrivateKey : PrivateKey? = null
    private val unlockState : MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (getEncryptionState() != EncryptionState.DISABLED) {
                val encryptedUserKey = localSource.getEncryptedUserKey() ?: throw IOException()
                userPublicKey = encryptionManager.getPublicKeyByEncryptedPair(encryptedUserKey)
            }
        }
    }

    override suspend fun listenToUnlockedState(): StateFlow<Boolean> = unlockState.asStateFlow()

    override suspend fun getEncryptionState(): EncryptionState {
        if (userPrivateKey != null) return EncryptionState.UNLOCKED
        if (userPublicKey != null) return EncryptionState.LOCKED
        var localKey = localSource.getEncryptedUserKey()
        if (localKey == null && settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            val remoteKey = remoteSource.getEncryptedUserKey()
            if (remoteKey != null) {
                localSource.setEncryptedUserKey(remoteKey)
                localKey = remoteKey
            }
        }
        return if (localKey != null) EncryptionState.LOCKED else EncryptionState.DISABLED
    }

    override suspend fun createEncryptedVault(password: String) {
        val kp = encryptionManager.generateKeyPair()
        val key = encryptionManager.generateSymmetricKey(password)
        val encryptedKeyPair = encryptionManager.encryptKeyPairBySymmetricKey(kp, key)

        localSource.setEncryptedUserKey(encryptedKeyPair)

        userPublicKey = kp.public
        userPrivateKey = kp.private

        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) remoteSource.setEncryptedUserKey(encryptedKeyPair)
    }

    override suspend fun unlockEncryptedVault(password: String) {
        val key = encryptionManager.generateSymmetricKey(password)
        val encryptedUserKey = localSource.getEncryptedUserKey() ?: throw IOException()
        val keyPair = encryptionManager.decryptKeyPairBySymmetricKey(encryptedUserKey, key)
        userPublicKey = keyPair.public
        userPrivateKey = keyPair.private
        unlockState.emit(true)
    }

    override suspend fun lockEncryptedVault() {
        userPrivateKey = null
        unlockState.emit(false)
    }

    override suspend fun deleteEncryptedVault() {
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) remoteSource.deleteEncryptedUserKey()
        localSource.deleteEncryptedUserKey()
    }

    override suspend fun setRecipeKey(localId: Int, remoteId: Int?, recipeKey: SecretKey) {
        val currentPublicKey = userPublicKey ?: throw IOException()
        val encryptedRecipeKey = encryptionManager.encryptSymmetricKeyByPrivateKey(recipeKey, currentPublicKey)
        localSource.setEncryptedRecipeKey(localId, encryptedRecipeKey)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && remoteId != null) remoteSource.setEncryptedRecipeKey(remoteId, encryptedRecipeKey)
    }

    override suspend fun getRecipeKey(localId: Int, remoteId: Int?): SecretKey {
        val currentPrivateKey = userPrivateKey ?: throw IOException()
        var encryptedKey = localSource.getEncryptedRecipeKey(localId)
        if (encryptedKey == null) {
            if (settings.getDataSourceType() != SettingsProto.DataSource.REMOTE || remoteId == null) throw IOException()
            encryptedKey = remoteSource.getEncryptedRecipeKey(remoteId)
            if (encryptedKey == null) throw IOException()
            localSource.setEncryptedRecipeKey(localId, encryptedKey)
        }
        return encryptionManager.decryptSymmetricKeyByPrivateKey(encryptedKey, currentPrivateKey)
    }

    override suspend fun decryptRecipeData(localId: Int, remoteId: Int?, encryptedData: ByteArray): ByteArray {
        val key = getRecipeKey(localId, remoteId)
        return encryptionManager.decryptDataBySymmetricKey(encryptedData, key)
    }

    override suspend fun deleteRecipeKey(localId: Int, remoteId: Int?) {
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && remoteId != null) remoteSource.deleteEncryptedRecipeKey(remoteId)
        localSource.deleteEncryptedRecipeKey(localId)
    }
}