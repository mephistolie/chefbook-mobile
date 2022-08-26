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

    override suspend fun setRecipeLikes(recipeId: Int, likes: Int?) =
        RoomHandler.handleQuery {
            dao.setLikes(recipeId, likes)
            SuccessResult
        }

    override suspend fun setRecipeLikeStatus(recipeId: Int, isLiked: Boolean) =
        RoomHandler.handleQuery {
            dao.setRecipeLiked(recipeId, isLiked)
            SuccessResult
        }

    override suspend fun setRecipeFavouriteStatus(recipeId: Int, isFavourite: Boolean) =
        RoomHandler.handleQuery { DataResult(dao.setRecipeFavourite(recipeId, isFavourite)).asEmpty() }

    override suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>) =
        RoomHandler.handleQuery {
            dao.setRecipeCategories(recipeId, categories)
            SuccessResult
        }

}
