package com.cactusknights.chefbook.repositories.sync

import android.util.Log
import com.cactusknights.chefbook.domain.CategoriesRepository
import com.cactusknights.chefbook.domain.RecipesRepository
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.DataSource
import com.cactusknights.chefbook.models.Recipe

import com.cactusknights.chefbook.repositories.local.datasources.LocalRecipesDataSource
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteRecipesDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.abs

class SyncRecipesRepository @Inject constructor(
    private val localSource: LocalRecipesDataSource,
    private val remoteSource: RemoteRecipesDataSource,
    private val categoriesSource: CategoriesRepository,
    private val settings: SyncSettingsRepository
) : RecipesRepository {

    companion object {
        const val SYNC_TIMEOUT = 60000
        const val TIMESTAMP_DELTA = 10000
    }

    private val recipes: MutableStateFlow<ArrayList<Recipe>> = MutableStateFlow(arrayListOf())
    private var categories: List<Category> = listOf()

    private val syncMutex = Mutex()
    private val categoriesMutex = Mutex()

    private var syncTimestamp : Date? = null

    override suspend fun listenToUserRecipes(): StateFlow<ArrayList<Recipe>> {
        syncMutex.withLock {
            if (syncTimestamp == null || abs(Date().time - syncTimestamp!!.time) > SYNC_TIMEOUT) getRecipes()
            syncTimestamp = Date()
        }
        return recipes.asStateFlow()
    }

    override suspend fun getRecipes(): ArrayList<Recipe> {
        val localRecipes = localSource.getRecipes(); recipes.emit(localRecipes)
        categoriesMutex.withLock {
            categories = categoriesSource.getCategories()
        }

        return if (settings.getDataSourceType() == DataSource.REMOTE) {
            try {
                val remoteRecipes = remoteSource.getRecipes()
                val preparedRemoteRecipes = prepareRemoteRecipes(remoteRecipes, localRecipes);
                recipes.emit(preparedRemoteRecipes)
                CoroutineScope(Dispatchers.IO).launch { syncRecipes(localRecipes, preparedRemoteRecipes) }
                preparedRemoteRecipes
            } catch (e: Exception) { localRecipes }
        } else { localRecipes }
    }

    override suspend fun addRecipe(recipe: Recipe) : Recipe {
        recipe.creationTimestamp = Date(); recipe.updateTimestamp = recipe.creationTimestamp
        recipe.id = localSource.addRecipe(recipe).id

        if (settings.getDataSourceType() == DataSource.REMOTE) {
            try {
                recipe.remoteId = remoteSource.addRecipe(recipe).remoteId
                localSource.updateRecipe(recipe)
            } catch (e: Exception) {}
        }
        recipes.value.add(recipe)
        recipes.emit(recipes.value)
        return recipe
    }

    override suspend fun getRecipe(recipeId: Int): Recipe {
        val localRecipe = localSource.getRecipe(recipeId)
        val remoteRecipe = try { remoteSource.getRecipe(recipeId) } catch (e: Exception) { null }
        val syncedRecipe = syncRecipe(localRecipe, remoteRecipe)
        if (syncedRecipe != null) return syncedRecipe else throw IOException()
    }

    override suspend fun getRecipeByRemoteId(remoteId: Int): Recipe {
        val localRecipe = localSource.getRecipeByRemoteId(remoteId)
        val remoteRecipe = try { remoteSource.getRecipe(remoteId) } catch (e: Exception) { null }
        return if (localRecipe != null) {
            val syncedRecipe = syncRecipe(localRecipe, remoteRecipe)
            syncedRecipe ?: throw IOException()
        } else remoteRecipe ?: throw IOException()
    }

    override suspend fun updateRecipe(recipe: Recipe) : Recipe {
        var updatedRecipe = recipe
        updatedRecipe.updateTimestamp = Date()
        localSource.updateRecipe(updatedRecipe)
        if (settings.getDataSourceType() == DataSource.REMOTE) {
            updatedRecipe = remoteSource.updateRecipe(updatedRecipe)
            localSource.updateRecipe(updatedRecipe)
        }
        recipes.emit(recipes.value.map { if (it.id == updatedRecipe.id) updatedRecipe else it} as ArrayList<Recipe>)
        return updatedRecipe
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        localSource.deleteRecipe(recipe)

        if (settings.getDataSourceType() == DataSource.REMOTE) {
            try {
                remoteSource.deleteRecipe(recipe)
            } catch (e: Exception) {}
            recipes.emit(recipes.value.filter { it.id != recipe.id } as ArrayList<Recipe>)
        }
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        localSource.setRecipeFavouriteStatus(recipe)
        if (settings.getDataSourceType() == DataSource.REMOTE) { remoteSource.setRecipeFavouriteStatus(recipe) }
        recipes.emit(recipes.value.map { if (it.id == recipe.id) recipe else it} as ArrayList<Recipe>)
    }

    override suspend fun setRecipeCategories(recipe: Recipe) {

        categoriesMutex.withLock {
            categories = categoriesSource.getCategories()
        }

        localSource.setRecipeCategories(recipe)
        if (settings.getDataSourceType() == DataSource.REMOTE) {
            try {
                recipe.categories = convertLocalCategoryIdsToRemote(recipe.categories)
                remoteSource.setRecipeCategories(recipe)
                recipe.categories = convertRemoteCategoryIdsToLocal(recipe.categories)
            } catch (e: Exception) {}
        }
        recipes.emit(recipes.value.map { if (it.id == recipe.id) recipe else it} as ArrayList<Recipe>)
    }

    override suspend fun setRecipeLikeStatus(recipe: Recipe) {
        localSource.setRecipeLikeStatus(recipe)
        if (settings.getDataSourceType() == DataSource.REMOTE) {
            remoteSource.setRecipeLikeStatus(recipe)
            recipes.emit(recipes.value.map { if (it.id == recipe.id) recipe else it} as ArrayList<Recipe>)
        }
    }

    private suspend fun syncRecipes(localData: ArrayList<Recipe>, remoteData: ArrayList<Recipe>) {
        val syncedRecipes = arrayListOf<Recipe>()
        var filteredRemoteRecipes = remoteData

        for (localRecipe in localData) {
            val remoteRecipe = filteredRemoteRecipes.filter { it.remoteId == localRecipe.remoteId }.getOrNull(0)
            val syncedRecipe = syncRecipe(localRecipe, remoteRecipe)
            if (syncedRecipe != null) syncedRecipes.add(syncedRecipe);
            filteredRemoteRecipes = filteredRemoteRecipes.filter { it.remoteId != localRecipe.remoteId } as ArrayList<Recipe>
        }

        val deletedIds = localSource.getDeletedRecipeRemoteIds()
        for (remoteRecipe in filteredRemoteRecipes) {
            if (remoteRecipe.remoteId in deletedIds) remoteSource.deleteRecipe(remoteRecipe)
            else {
                remoteRecipe.id = localSource.addRecipe(remoteRecipe).id
                syncedRecipes.add(remoteRecipe)
            }
        }

        recipes.emit(syncedRecipes)
    }

    private suspend fun syncRecipe(localRecipe: Recipe, remoteRecipe: Recipe? = null) : Recipe? {
        var syncedRecipe = localRecipe

        // Send recipe from local source to remote source
        if (localRecipe.remoteId == null) {
            localRecipe.remoteId = remoteSource.addRecipe(localRecipe).remoteId
            localSource.updateRecipe(localRecipe)
            return syncedRecipe
        }

        // Remove deleted recipe from local source
        if (remoteRecipe == null) { localSource.deleteRecipe(localRecipe); return null }

        if (abs(localRecipe.updateTimestamp.time - remoteRecipe.updateTimestamp.time) > TIMESTAMP_DELTA) {
            if (localRecipe.updateTimestamp > remoteRecipe.updateTimestamp) {
                localRecipe.categories = convertLocalCategoryIdsToRemote(localRecipe.categories)
                remoteSource.updateRecipe(localRecipe)
                localRecipe.categories = convertRemoteCategoryIdsToLocal(localRecipe.categories)
            } else {
                remoteRecipe.categories = convertRemoteCategoryIdsToLocal(remoteRecipe.categories)
                localSource.updateRecipe(remoteRecipe); syncedRecipe = remoteRecipe
            }
        }

        if (remoteRecipe.isFavourite != localRecipe.isFavourite) {
            localRecipe.isFavourite = remoteRecipe.isFavourite; localSource.setRecipeFavouriteStatus(localRecipe)
        }

        var isChanged = false
        if (syncedRecipe.isLiked != remoteRecipe.isLiked) { syncedRecipe.isLiked = remoteRecipe.isLiked; isChanged = true }
        if (syncedRecipe.likes != remoteRecipe.likes) { syncedRecipe.likes = remoteRecipe.likes; isChanged = true }
        if (syncedRecipe.ownerId != remoteRecipe.ownerId) { syncedRecipe.ownerId = remoteRecipe.ownerId; isChanged = true }
        if (syncedRecipe.ownerName != remoteRecipe.ownerName) { syncedRecipe.ownerName = remoteRecipe.ownerName; isChanged = true }
        if (syncedRecipe.isOwned != remoteRecipe.isOwned) { syncedRecipe.isOwned = remoteRecipe.isOwned; isChanged = true }
        if (isChanged) localSource.updateRecipe(syncedRecipe)

        return syncedRecipe
    }

    private suspend fun prepareRemoteRecipes(remoteRecipes : ArrayList<Recipe>, localRecipes: ArrayList<Recipe>) : ArrayList<Recipe> {
        setRecipeIds(localRecipes, remoteRecipes)
        remoteRecipes.map { it.categories = convertRemoteCategoryIdsToLocal(it.categories) }
        return remoteRecipes
    }

    private fun setRecipeIds(localData: ArrayList<Recipe>, remoteData: ArrayList<Recipe>) {
        remoteData.map { remote -> remote.id = localData.filter { local -> local.remoteId == remote.remoteId}.getOrNull(0)?.id }
    }

    private suspend fun convertLocalCategoryIdsToRemote(localIds: ArrayList<Int>) : ArrayList<Int> {
        categoriesMutex.withLock {
            return categories.filter { it.id in localIds && it.remoteId != null }.map { it.remoteId!! } as ArrayList<Int>
        }
    }


    private suspend fun convertRemoteCategoryIdsToLocal(remoteIds: ArrayList<Int>) : ArrayList<Int> {
        categoriesMutex.withLock {
            return categories.filter { it.remoteId in remoteIds && it.id != null }.map { it.id!! } as ArrayList<Int>
        }
    }
}