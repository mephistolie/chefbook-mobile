package com.cactusknights.chefbook.data.repositories.recipes

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.cache.CategoriesCacheReader
import com.cactusknights.chefbook.core.cache.RecipeBookCacheManager
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.data.LocalRecipeInteractionDataSource
import com.cactusknights.chefbook.data.RecipeInteractionDataSource
import com.cactusknights.chefbook.domain.RecipeInteractionRepo
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.data.repositories.sync.RecipeBookRepoImpl
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList
import kotlin.math.abs

@Singleton
class RecipeInteractionRepoImpl @Inject constructor(
    @Local private val localSource: LocalRecipeInteractionDataSource,
    @Remote private val remoteSource: RecipeInteractionDataSource,

    private val cache: RecipeBookCacheManager,
    private val categoriesCache: CategoriesCacheReader,
    private val settings: SettingsManager
) : RecipeInteractionRepo {

    private var categories: List<Category>? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            categoriesCache.listenToCategories().collect {
                categories = it
            }
        }
    }

    override suspend fun syncRecipeInteractions(local: RecipeInfo, remote: RecipeInfo) : RecipeInfo {
        var syncedRecipeInfo = local
        if (abs(local.userTimestamp.time - remote.userTimestamp.time) > RecipeBookRepoImpl.TIMESTAMP_DELTA) {
            if (local.userTimestamp < remote.userTimestamp) {
                localSource.setRecipeFavouriteStatus(local.id!!, remote.isFavourite)
                localSource.setRecipeCategories(local.id!!, getLocalCategoriesByRemote(remote.categories))
                syncedRecipeInfo = local
            } else {
                remoteSource.setRecipeFavouriteStatus(remote.remoteId!!, local.isFavourite)
                remote.categories = getRemoteCategoriesByLocal(local.categories)
                remoteSource.setRecipeCategories(remote.remoteId!!, remote.categories)
                syncedRecipeInfo = remote
            }
            localSource.updateRecipeInteractionTimestamp(local.id!!)
        }
        cache.updateRecipe(syncedRecipeInfo)
        return syncedRecipeInfo
    }

    override suspend fun setRecipeLikeStatus(recipe: Recipe) {
        localSource.setRecipeLikeStatus(recipe.id!!, recipe.isLiked)
        localSource.updateRecipeInteractionTimestamp(recipe.id!!)

        cache.updateRecipe(recipe.info())
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            val remoteId = recipe.remoteId ?: return
            remoteSource.setRecipeLikeStatus(remoteId, recipe.isLiked)
        }
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        localSource.setRecipeFavouriteStatus(recipe.id!!, recipe.isFavourite)
        localSource.updateRecipeInteractionTimestamp(recipe.id!!)

        cache.updateRecipe(recipe.info())
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            val remoteId = recipe.remoteId ?: return
            remoteSource.setRecipeFavouriteStatus(remoteId, recipe.isFavourite)
        }
    }

    override suspend fun setRecipeCategories(recipe: Recipe) {
        localSource.setRecipeCategories(recipe.id!!, recipe.categories)
        localSource.updateRecipeInteractionTimestamp(recipe.id!!)

        cache.updateRecipe(recipe.info())
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && recipe.remoteId != null) {
            val remoteId = recipe.remoteId ?: return
            val remoteCategories = getRemoteCategoriesByLocal(recipe.categories) as ArrayList<Int>
            remoteSource.setRecipeCategories(remoteId, remoteCategories)
        }
    }

    override suspend fun setRecipeRemoteCategories(recipe: Recipe) {
        recipe.categories = getLocalCategoriesByRemote(recipe.categories) as ArrayList<Int>
        localSource.setRecipeCategories(recipe.id!!, recipe.categories)
        localSource.updateRecipeInteractionTimestamp(recipe.id!!)
        cache.updateRecipe(recipe.info())
    }

    private fun getLocalCategoriesByRemote(remote: List<Int>) : List<Int> {
        return categories?.filter { it.remoteId in remote }?.mapNotNull { it.id } ?: listOf()
    }

    private fun getRemoteCategoriesByLocal(local: List<Int>) : List<Int> {
        return categories?.filter { it.id in local }?.mapNotNull { it.remoteId } ?: listOf()
    }
}