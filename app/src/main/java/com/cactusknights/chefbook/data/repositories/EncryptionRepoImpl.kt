package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.encryption.EncryptionManager
import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.data.EncryptionDataSource
import com.cactusknights.chefbook.domain.VaultEncryptionRepo
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.RecipeEncryptionRepo
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
class EncryptionRepoImpl @Inject constructor(
    @Local private val localSource: EncryptionDataSource,
    @Remote private val remoteSource: EncryptionDataSource,

    private val encryptionManager: EncryptionManager,
    private val settings: SettingsManager
): VaultEncryptionRepo, RecipeEncryptionRepo {

    private var userKeys : KeyPair? = null
    private val unlockState : MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getEncryptionState()
        }
    }

    override suspend fun listenToUnlockedState(): StateFlow<Boolean> = unlockState.asStateFlow()

    override suspend fun getEncryptionState(): EncryptionState {
        if (userKeys != null) return EncryptionState.UNLOCKED
        var localKey = localSource.getEncryptedUserKey()
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            val remoteKey = remoteSource.getEncryptedUserKey()
            if (localKey != null && remoteKey == null) {
                remoteSource.setEncryptedUserKey(localKey)
            } else if (remoteKey != null) {
                localSource.setEncryptedUserKey(remoteKey)
                localKey = remoteKey
            }
        }
        return if (localKey != null) EncryptionState.LOCKED else EncryptionState.DISABLED
    }

    override suspend fun createEncryptedVault(password: String) {
        userKeys = encryptionManager.generateKeyPair()
        val key = encryptionManager.generateSymmetricKey(password)
        val encryptedKeyPair = encryptionManager.encryptKeyPairBySymmetricKey(userKeys!!, key)

        localSource.setEncryptedUserKey(encryptedKeyPair)

        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) remoteSource.setEncryptedUserKey(encryptedKeyPair)
    }

    override suspend fun unlockEncryptedVault(password: String) {
        val key = encryptionManager.generateSymmetricKey(password)
        val encryptedUserKey = localSource.getEncryptedUserKey() ?: throw IOException()
        userKeys = encryptionManager.decryptKeyPairBySymmetricKey(encryptedUserKey, key)
        unlockState.emit(true)
    }

    override suspend fun lockEncryptedVault() {
        userKeys = null
        unlockState.emit(false)
    }

    override suspend fun deleteEncryptedVault() {
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) remoteSource.deleteEncryptedUserKey()
        localSource.deleteEncryptedUserKey()
    }

    override suspend fun syncRecipeKey(localId: Int, remoteId: Int) {
        val localKey = localSource.getEncryptedRecipeKey(localId)
        val remoteKey = remoteSource.getEncryptedRecipeKey(remoteId)
        if (localKey != null && remoteKey == null) {
            remoteSource.setEncryptedRecipeKey(remoteId, localKey)
        } else if (remoteKey != null && localKey == null) {
            localSource.setEncryptedRecipeKey(localId, remoteKey)
        }
    }

    override suspend fun setRecipeKey(localId: Int, remoteId: Int?, recipeKey: SecretKey) {
        val currentPublicKey = userKeys?.public ?: throw IOException()
        val encryptedRecipeKey = encryptionManager.encryptSymmetricKeyByPrivateKey(recipeKey, currentPublicKey)
        localSource.setEncryptedRecipeKey(localId, encryptedRecipeKey)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && remoteId != null) remoteSource.setEncryptedRecipeKey(remoteId, encryptedRecipeKey)
    }

    override suspend fun getRecipeKey(localId: Int, remoteId: Int?): SecretKey {
        val currentPrivateKey = userKeys?.private ?: throw IOException()
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