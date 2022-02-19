package com.cactusknights.chefbook.data.sources.local.datasources.recipes

import com.cactusknights.chefbook.data.LocalRecipeInteractionDataSource
import com.cactusknights.chefbook.data.sources.local.dao.RecipeInteractionDao
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipeInteractionDataSourceImpl @Inject constructor(
    private val dao: RecipeInteractionDao,
) : LocalRecipeInteractionDataSource {

    override suspend fun setRecipeLikeStatus(recipeId: Int, isLiked: Boolean) {
        dao.setRecipeLiked(recipeId, isLiked)
        if (isLiked) dao.increaseLikes(recipeId) else dao.reduceLikes(recipeId)
    }

    override suspend fun setRecipeFavouriteStatus(recipeId: Int, isFavourite: Boolean) {
        dao.setRecipeFavourite(recipeId, isFavourite)
    }

    override suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>) {
        dao.deleteRecipeCategories(recipeId)
        dao.addRecipeCategories(recipeId, categories)
    }

    override suspend fun updateRecipeInteractionTimestamp(recipeId: Int, timestamp: Date) {
        dao.updateRecipeInteractionTimestamp(recipeId, timestamp)
    }
}