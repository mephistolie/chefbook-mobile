package com.mysty.chefbook.api.recipe.data.repositories

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.crypto.IHybridCryptor
import com.mysty.chefbook.api.encryption.data.ILocalEncryptionSource
import com.mysty.chefbook.api.encryption.data.IRemoteEncryptionSource
import com.mysty.chefbook.api.recipe.domain.IRecipeEncryptionRepo
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey

internal class RecipeEncryptionRepo(
    private val localSource: ILocalEncryptionSource,
    private val remoteSource: IRemoteEncryptionSource,

    private val cryptor: IHybridCryptor,
    private val sourceRepo: ISourcesRepo,
): IRecipeEncryptionRepo {

    override suspend fun getRecipeKey(recipeId: String, userKey: PrivateKey): ActionStatus<SecretKey> {
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
        recipeId: String,
        recipeKey: SecretKey,
        userKey: PublicKey
    ): SimpleAction {
        val encryptedRecipeKey = cryptor.encryptSymmetricKeyByPrivateKey(recipeKey, userKey)
        var result = localSource.setRecipeKey(recipeId, encryptedRecipeKey)
        if (sourceRepo.isOnlineMode()) result = remoteSource.setRecipeKey(recipeId, encryptedRecipeKey)

        return result
    }

    override suspend fun deleteRecipeKey(recipeId: String): SimpleAction {
        var result = localSource.deleteRecipeKey(recipeId)
        if (sourceRepo.isOnlineMode()) result = remoteSource.deleteRecipeKey(recipeId)

        return result
    }

}
