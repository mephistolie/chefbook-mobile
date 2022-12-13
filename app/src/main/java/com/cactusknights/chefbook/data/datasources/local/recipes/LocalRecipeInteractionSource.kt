package com.cactusknights.chefbook.data.datasources.local.recipes

import com.cactusknights.chefbook.data.ILocalRecipeInteractionSource
import com.cactusknights.chefbook.data.room.RoomHandler
import com.cactusknights.chefbook.data.room.dao.RecipeInteractionDao
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipeInteractionSource @Inject constructor(
    private val dao: RecipeInteractionDao,
) : ILocalRecipeInteractionSource {

    override suspend fun setRecipeLikes(recipeId: String, likes: Int?) =
        RoomHandler.handleQuery {
            dao.setLikes(recipeId, likes)
            SuccessResult
        }

    override suspend fun setRecipeLikeStatus(recipeId: String, isLiked: Boolean) =
        RoomHandler.handleQuery {
            dao.setRecipeLiked(recipeId, isLiked)
            SuccessResult
        }

    override suspend fun setRecipeFavouriteStatus(recipeId: String, isFavourite: Boolean) =
        RoomHandler.handleQuery { DataResult(dao.setRecipeFavourite(recipeId, isFavourite)).asEmpty() }

    override suspend fun setRecipeCategories(recipeId: String, categories: List<String>) =
        RoomHandler.handleQuery {
            dao.setRecipeCategories(recipeId, categories)
            SuccessResult
        }

}
