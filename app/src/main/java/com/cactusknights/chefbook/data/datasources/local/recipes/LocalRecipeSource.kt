package com.cactusknights.chefbook.data.datasources.local.recipes

import com.cactusknights.chefbook.data.ILocalRecipeSource
import com.cactusknights.chefbook.data.dto.local.room.toEntity
import com.cactusknights.chefbook.data.dto.local.room.toRoom
import com.cactusknights.chefbook.data.room.RoomHandler
import com.cactusknights.chefbook.data.room.dao.RecipeBookDao
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.entities.recipe.toRecipeInfo
import javax.inject.Singleton

@Singleton
class LocalRecipeSource(
    private val dao: RecipeBookDao,
) : ILocalRecipeSource {

    override suspend fun getRecipeBook() =
        RoomHandler.handleQuery {
            val recipes = arrayListOf<RecipeInfo>()
            dao.getRecipes().map { it.toEntity().toRecipeInfo() }.toCollection(recipes)
            recipes.forEachIndexed { index, recipe ->
                recipes[index] = recipes[index].copy(
                    categories = dao.getRecipeCategories(recipe.id).map { it.toEntity() }
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
            val recipe = baseRecipe.copy(categories = dao.getRecipeCategories(baseRecipe.id).map { it.toEntity() })
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
