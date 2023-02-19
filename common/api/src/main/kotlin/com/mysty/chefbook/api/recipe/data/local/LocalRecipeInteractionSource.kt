package com.mysty.chefbook.api.recipe.data.local

import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.room.RoomHandler
import com.mysty.chefbook.api.recipe.data.local.dao.RecipeInteractionDao
import com.mysty.chefbook.api.recipe.data.repositories.ILocalRecipeInteractionSource

internal class LocalRecipeInteractionSource(
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
