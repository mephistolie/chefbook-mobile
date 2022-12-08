package com.cactusknights.chefbook.data.repositories.recipe

import com.cactusknights.chefbook.core.encryption.IHybridCryptor
import com.cactusknights.chefbook.data.ILocalEncryptionSource
import com.cactusknights.chefbook.data.IRemoteEncryptionSource
import com.cactusknights.chefbook.data.repositories.SourceRepo
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeEncryptionRepo @Inject constructor(
    @Local private val localSource: ILocalEncryptionSource,
    @Remote private val remoteSource: IRemoteEncryptionSource,

    private val cryptor: IHybridCryptor,
    private val sourceRepo: SourceRepo,
): IRecipeEncryptionRepo {

    override suspend fun getRecipeKey(recipeId: Int, userKey: PrivateKey): ActionStatus<SecretKey> {
        var encryptedKeyResult = localSource.getRecipeKey(recipeId)
        if (encryptedKeyResult.isFailure() && sourceRepo.isOnlineMode()) {
            encryptedKeyResult = remoteSource.getRecipeKey(recipeId)
            if (encryptedKeyResult.isSuccess()) localSource.setRecipeKey(recipeId, encryptedKeyResult.data())
        }
        if (encryptedKeyResult.isFailure()) return encryptedKeyResult.asFailure()

        val key = cryptor.decryptSymmetricKeyByPrivateKey(encryptedKeyResult.data(), userKey)

        return DataResult(key)
    }

    override suspend fun setRecipeKey(
        recipeId: Int,
        recipeKey: SecretKey,
        userKey: PublicKey
    ): SimpleAction {
        val encryptedRecipeKey = cryptor.encryptSymmetricKeyByPrivateKey(recipeKey, userKey)
        var result = localSource.setRecipeKey(recipeId, encryptedRecipeKey)
        if (sourceRepo.isOnlineMode()) result = remoteSource.setRecipeKey(recipeId, encryptedRecipeKey)

        return result
    }

    override suspend fun deleteRecipeKey(recipeId: Int): SimpleAction {
        var result = localSource.deleteRecipeKey(recipeId)
        if (sourceRepo.isOnlineMode()) result = remoteSource.deleteRecipeKey(recipeId)

        return result
    }

}
