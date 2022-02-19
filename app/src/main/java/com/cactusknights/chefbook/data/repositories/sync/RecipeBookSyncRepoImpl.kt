package com.cactusknights.chefbook.data.repositories.sync

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.cache.RecipeBookCacheManager
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.encryption.Cryptor
import com.cactusknights.chefbook.core.encryption.EncryptionState
import com.cactusknights.chefbook.data.LocalRecipeBookDataSource
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.data.RecipeCrudDataSource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class RecipeBookRepoImpl @Inject constructor(
    @Local private val localSource: LocalRecipeBookDataSource,
    @Remote private val remoteSource: RecipeCrudDataSource,

    private val interactionRepo: RecipeInteractionRepo,
    private val picturesRepo: RecipePicturesRepo,
    private val recipesEncryptionRepo: RecipeEncryptionRepo,
    private val categoriesRepo: CategoriesSyncRepo,
    private val vaultEncryptionRepo: VaultEncryptionRepo,

    private val cache: RecipeBookCacheManager,
    private val cryptor: Cryptor,
    private val settings: SettingsManager,
) : RecipeBookSyncRepo {

    init {
        CoroutineScope(Dispatchers.IO).launch { syncRecipeBook() }
    }

    override suspend fun listenToRecipeBook(): StateFlow<List<RecipeInfo>?> = cache.listenToRecipeBook()
    override suspend fun getRecipeBook(): List<RecipeInfo> = cache.getRecipeBook()

    override suspend fun syncRecipeBook() {
        categoriesRepo.syncCategories()
        val localRecipes = localSource.getRecipeBook(); cache.setRecipes(localRecipes)

        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            val remoteRecipesInfo = fillRecipesLocalId(localRecipes, remoteSource.getRecipeBook())
            cache.setRecipes(remoteRecipesInfo)
            val syncedRecipesInfo = syncRecipes(localRecipes, remoteRecipesInfo)
            cache.setRecipes(syncedRecipesInfo)
        }
    }

    private suspend fun syncRecipes(local: List<RecipeInfo>, remote: List<RecipeInfo>) : List<RecipeInfo> {

        val syncedRecipesInfo = arrayListOf<RecipeInfo>()
        var uncachedRecipes = remote

        val pushedRecipes = syncCachedRecipes(local, uncachedRecipes)
        val pushedRecipesId = pushedRecipes.map { it.remoteId }
        uncachedRecipes = uncachedRecipes.filter { it.remoteId !in pushedRecipesId }
        syncedRecipesInfo.addAll(pushedRecipes)

        val pulledRecipes = syncUncachedRecipes(uncachedRecipes)
        syncedRecipesInfo.addAll(pulledRecipes)

        return syncedRecipesInfo
    }

    private suspend fun syncCachedRecipes(cachedRecipes : List<RecipeInfo>, uncachedRecipes: List<RecipeInfo>) : List<RecipeInfo> {
        val syncedRecipes = arrayListOf<RecipeInfo>()
        for (localRecipe in cachedRecipes) {
            val remoteRecipe = uncachedRecipes.firstOrNull { it.remoteId == localRecipe.remoteId }
            val syncedRecipe = syncCachedRecipe(localRecipe, remoteRecipe)
            if (syncedRecipe != null) syncedRecipes.add(syncedRecipe)
        }
        return syncedRecipes
    }

    private suspend fun syncCachedRecipe(localInfo: RecipeInfo, remoteInfo: RecipeInfo? = null) : RecipeInfo? {
        val localId = localInfo.id

        // Remove deleted recipe from local source
        if (remoteInfo == null) {
            localSource.deleteRecipe(localId!!)
            return null
        }

        val remoteId = localInfo.remoteId?: remoteInfo.remoteId

        // Send recipe from local source to remote source
        if (remoteId == null && localId != null) {
            val localRecipe = localSource.getRecipeById(localId)
            val remoteRecipe = remoteSource.createRecipe(localRecipe)
            localRecipe.remoteId = remoteRecipe.remoteId

            val processedRecipe = pushRecipeContentChanges(localRecipe, remoteSource)
            interactionRepo.setRecipeCategories(localRecipe)
            interactionRepo.setRecipeFavouriteStatus(localRecipe)
            if (processedRecipe.isEncrypted) recipesEncryptionRepo.syncRecipeKey(processedRecipe.id!!, processedRecipe.remoteId!!)

            localSource.updateRecipe(processedRecipe)
            return processedRecipe.info()
        }

        if (localInfo.isEncrypted) recipesEncryptionRepo.syncRecipeKey(localId!!, remoteId!!)
        interactionRepo.syncRecipeInteractions(localInfo, remoteInfo)
        syncRecipeContentChanges(localInfo, remoteInfo)

        return pullRemoteUpdates(localId!!, remoteInfo)
    }

    private suspend fun syncUncachedRecipes(uncachedRecipes : List<RecipeInfo>) : List<RecipeInfo> {
        val syncedRecipes = arrayListOf<RecipeInfo>()
        val deletedIds = localSource.getDeletedRecipeRemoteIds()
        for (uncachedRecipe in uncachedRecipes) {
            if (uncachedRecipe.remoteId in deletedIds) remoteSource.deleteRecipe(uncachedRecipe.remoteId!!)
            else {
                val remoteRecipe = remoteSource.getRecipeById(uncachedRecipe.remoteId!!)
                remoteRecipe.id = localSource.createRecipe(remoteRecipe).id

                interactionRepo.setRecipeRemoteCategories(remoteRecipe)
                if (remoteRecipe.isEncrypted) recipesEncryptionRepo.syncRecipeKey(remoteRecipe.id!!, remoteRecipe.remoteId!!)
                syncedRecipes.add(remoteRecipe.info())
            }
        }
        return syncedRecipes
    }

    private suspend fun syncRecipeContentChanges(localInfo: RecipeInfo, remoteInfo: RecipeInfo) : Recipe {
        var syncedRecipe : Recipe = DecryptedRecipe()
        if (abs(localInfo.updateTimestamp.time - remoteInfo.updateTimestamp.time) > TIMESTAMP_DELTA) {
            if (localInfo.updateTimestamp.time > remoteInfo.updateTimestamp.time && remoteInfo.isOwned) {
                syncedRecipe = pushRecipeContentChanges(localSource.getRecipeById(localInfo.id!!), remoteSource)
            } else {
                val remoteRecipe = remoteSource.getRecipeById(remoteInfo.remoteId!!)
                remoteRecipe.id = localInfo.id
                syncedRecipe = pushRecipeContentChanges(remoteRecipe, localSource)
            }
        }
        return syncedRecipe
    }

    private suspend fun pushRecipeContentChanges(recipe: Recipe, source: RecipeCrudDataSource) : Recipe {
        if (recipe !is DecryptedRecipe && vaultEncryptionRepo.getEncryptionState() != EncryptionState.UNLOCKED) return recipe

        var recipeKey : SecretKey? = null
        var decryptedRecipe = if (recipe is DecryptedRecipe) recipe else {
            recipeKey = recipesEncryptionRepo.getRecipeKey(recipe.id!!, recipe.remoteId)
            (recipe as EncryptedRecipe).decrypt { data -> cryptor.decryptDataBySymmetricKey(data, recipeKey) }
        }

        decryptedRecipe = picturesRepo.syncRecipePictures(decryptedRecipe, recipeKey)
        val processedRecipe = if (recipeKey == null) decryptedRecipe else decryptedRecipe.encrypt { data -> cryptor.encryptDataBySymmetricKey(data, recipeKey) }

        return source.updateRecipe(processedRecipe)
    }

    private suspend fun pullRemoteUpdates(localId: Int, remoteInfo: RecipeInfo) : RecipeInfo {
        val recipe = localSource.getRecipeById(localId)
        var isChanged = false
        if (recipe.isLiked != remoteInfo.isLiked) { recipe.isLiked = remoteInfo.isLiked; isChanged = true }
        if (recipe.likes != remoteInfo.likes) { recipe.likes = remoteInfo.likes; isChanged = true }
        if (recipe.ownerId != remoteInfo.ownerId) { recipe.ownerId = remoteInfo.ownerId; isChanged = true }
        if (recipe.ownerName != remoteInfo.ownerName) { recipe.ownerName = remoteInfo.ownerName; isChanged = true }
        if (recipe.isOwned != remoteInfo.isOwned) { recipe.isOwned = remoteInfo.isOwned; isChanged = true }
        if (isChanged) localSource.updateRecipe(recipe)
        return recipe.info()
    }

    companion object {
        const val SYNC_TIMEOUT = 60000
        const val TIMESTAMP_DELTA = 10000

        private fun fillRecipesLocalId(localData: List<RecipeInfo>, remoteData: List<RecipeInfo>) : List<RecipeInfo> {
            return remoteData.map { remote -> remote.id = localData.firstOrNull { local -> local.remoteId == remote.remoteId }?.id; remote }
        }
    }
}