package com.cactusknights.chefbook.data.repositories.recipes

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.common.deepCopy
import com.cactusknights.chefbook.core.cache.RecipeBookCacheManager
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.encryption.Cryptor
import com.cactusknights.chefbook.data.LocalRecipeBookDataSource
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.data.RecipeCrudDataSource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.*
import java.io.IOException
import java.util.*
import javax.crypto.SecretKey
import javax.inject.Inject

class RecipeCrudRepoImpl @Inject constructor(
    @Local private val localSource: LocalRecipeBookDataSource,
    @Remote private val remoteSource: RecipeCrudDataSource,

    private val picturesRepository: RecipePicturesRepo,
    private val encryptionRepository: RecipeEncryptionRepo,

    private val cache: RecipeBookCacheManager,
    private val cryptor: Cryptor,
    private val settings: SettingsManager
) : RecipeCrudRepo {

    override suspend fun createRecipe(recipe: DecryptedRecipe) : Recipe {
        var recipeWithoutPictures: Recipe = recipe.deepCopy().withoutPictures()

        // Encryption
        var recipeKey: SecretKey? = null
        if (recipeWithoutPictures.isEncrypted) {
            recipeKey = cryptor.generateSymmetricKey()
            recipeWithoutPictures = (recipeWithoutPictures as DecryptedRecipe).encrypt { data -> cryptor.encryptDataBySymmetricKey(data, recipeKey) }
        }

        // Base Recipe Uploading
        recipeWithoutPictures = localSource.createRecipe(recipeWithoutPictures)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            try {
                recipeWithoutPictures = remoteSource.createRecipe(recipeWithoutPictures)
                localSource.updateRecipe(recipeWithoutPictures)
            } catch (e: Exception) { }
        }


        var decryptedRecipe = recipe.copy(id = recipeWithoutPictures.id, remoteId = recipeWithoutPictures.remoteId)
        decryptedRecipe = picturesRepository.syncRecipePictures(decryptedRecipe, recipeKey)

        var fullRecipe: Recipe = decryptedRecipe

        // Key Uploading
        if (recipeKey != null) {
            encryptionRepository.setRecipeKey(recipeWithoutPictures.id!!, recipeWithoutPictures.remoteId, recipeKey)
            fullRecipe = decryptedRecipe.encrypt { data -> cryptor.encryptDataBySymmetricKey(data, recipeKey) }
        }

        // Full Recipe Uploading
        localSource.updateRecipe(fullRecipe)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && fullRecipe.remoteId != null) {
            remoteSource.updateRecipe(fullRecipe)
        }

        cache.addRecipe(fullRecipe.info())
        return fullRecipe
    }

    override suspend fun updateRecipe(recipe: DecryptedRecipe) : Recipe {
        var decryptedRecipe = recipe.copy(updateTimestamp = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis))
        var updatedRecipe : Recipe = decryptedRecipe

        var recipeKey: SecretKey? = null
        if (decryptedRecipe.isEncrypted) {
            recipeKey = encryptionRepository.getRecipeKey(decryptedRecipe.id!!, decryptedRecipe.remoteId)
        }

        decryptedRecipe = picturesRepository.syncRecipePictures(decryptedRecipe, recipeKey)

        if (recipeKey != null) {
            updatedRecipe = decryptedRecipe.encrypt { data -> cryptor.encryptDataBySymmetricKey(data, recipeKey) }
        }

        updatedRecipe = localSource.updateRecipe(updatedRecipe)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && updatedRecipe.remoteId != null) {
            updatedRecipe = remoteSource.updateRecipe(updatedRecipe)
            localSource.updateRecipe(updatedRecipe)
        }

        cache.updateRecipe(updatedRecipe.info())
        return updatedRecipe
    }

    override suspend fun getRecipeById(recipeId: Int): Recipe {
        return localSource.getRecipeById(recipeId)
    }

    override suspend fun getRecipeByRemoteId(remoteId: Int): Recipe {
        val localRecipe = localSource.getRecipeByRemoteId(remoteId)
        val remoteRecipe = try { remoteSource.getRecipeById(remoteId) } catch (e: Exception) { null }
        return if (localRecipe != null) {
            return localSource.getRecipeById(localRecipe.id!!)
        } else remoteRecipe ?: throw IOException()
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        localSource.deleteRecipe(recipe.id!!)

        val remoteId = recipe.remoteId
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && remoteId != null) {
            try {
                remoteSource.deleteRecipe(remoteId)
            } catch (e: Exception) {}
        }

        cache.deleteRecipe(recipe.info())
    }
}