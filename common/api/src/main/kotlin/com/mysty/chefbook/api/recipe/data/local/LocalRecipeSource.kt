package com.mysty.chefbook.api.recipe.data.local

import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity
import com.mysty.chefbook.api.category.data.local.tables.toEntity
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType
import com.mysty.chefbook.api.common.room.RoomHandler
import com.mysty.chefbook.api.recipe.data.local.dao.RecipeBookDao
import com.mysty.chefbook.api.recipe.data.local.tables.toRoom
import com.mysty.chefbook.api.recipe.data.repositories.ILocalRecipeSource
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.toRecipeInfo

internal class LocalRecipeSource(
    private val dao: RecipeBookDao,
) : ILocalRecipeSource {

    override suspend fun getRecipeBook() =
        RoomHandler.handleQuery {
            val recipes = arrayListOf<RecipeInfo>()
            dao.getRecipes().map { it.toEntity().toRecipeInfo() }.toCollection(recipes)
            recipes.forEachIndexed { index, recipe ->
                recipes[index] = recipes[index].copy(
                    categories = dao.getRecipeCategories(recipe.id).map(CategoryRoomEntity::toEntity)
                )
            }
            DataResult(recipes)
        }

    override suspend fun createRecipe(recipe: Recipe) =
        RoomHandler.handleQuery {
            dao.addRecipe(recipe.toRoom())
            SuccessResult
        }

    override suspend fun getRecipe(recipeId: String) =
        RoomHandler.handleQuery {
            val baseRecipe = dao.getRecipeById(recipeId)?.toEntity()
                ?: return@handleQuery Failure(AppError(AppErrorType.NOT_FOUND))
            val recipe = baseRecipe.copy(categories = dao.getRecipeCategories(baseRecipe.id).map(CategoryRoomEntity::toEntity))
            DataResult(recipe)
        }

    override suspend fun updateRecipe(recipe: Recipe) =
        RoomHandler.handleQuery {
            dao.updateRecipe(recipe.toRoom())
            SuccessResult
        }

    override suspend fun deleteRecipe(recipeId: String) =
        RoomHandler.handleQuery {
            dao.deleteRecipe(recipeId)
            SuccessResult
        }

}
