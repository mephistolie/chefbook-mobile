package com.cactusknights.chefbook.data.datasources.remote.recipes

import com.cactusknights.chefbook.core.mappers.LanguageMapper
import com.cactusknights.chefbook.data.IRemoteRecipeSource
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.dto.remote.common.isSuccess
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.dto.remote.recipe.toEntity
import com.cactusknights.chefbook.data.dto.remote.recipe.toSerializable
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.RecipeApi
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.RecipesFilter
import javax.inject.Inject

class RemoteRecipeSource @Inject constructor(
    private val api: RecipeApi,
    private val handleResponse: INetworkHandler,
) : IRemoteRecipeSource {

    override suspend fun addRecipeToRecipeBook(recipeId: Int): SimpleAction =
        handleResponse { api.addRecipeToRecipeBook(recipeId) }.toActionStatus().asEmpty()

    override suspend fun removeFromRecipeToRecipeBook(recipeId: Int): SimpleAction =
        handleResponse { api.removeRecipeFromRecipeBook(recipeId) }.toActionStatus().asEmpty()

    override suspend fun getRecipesByQuery(query: RecipesFilter): ActionStatus<List<RecipeInfo>> {
        val result = handleResponse {
            api.getRecipes(
                search = query.search,
                owned = query.saved,
                saved = query.saved,
                page = query.page,
                pageSize = query.pageSize,
                minTime = query.minTime,
                maxTime = query.maxTime,
                minServings = query.minServings,
                maxServings = query.maxServings,
                minCalories = query.minCalories,
                maxCalories = query.maxCalories,
                sortBy = query.sortBy.toString(),
                languages = query.languages?.map { LanguageMapper.map(it) }
            )
        }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().map { it.toEntity() })
    }

    override suspend fun getRecipeBook(): ActionStatus<List<RecipeInfo>> {
        val recipes = arrayListOf<RecipeInfo>()
        var page = 1
        var isEnd = false

        while (!isEnd) {
            val result = handleResponse { api.getRecipes(saved = true, page = page, pageSize = PAGE_SIZE) }
            if (result.isSuccess()) {
                val pageRecipes = result.body().map { it.toEntity() }
                recipes.addAll(pageRecipes)
                if (pageRecipes.size < PAGE_SIZE) isEnd = true
                page++
            } else isEnd = true
        }

        return DataResult(recipes)
    }

    override suspend fun createRecipe(input: RecipeInput): ActionStatus<Int> {
        val result = handleResponse { api.createRecipe(input.toSerializable()) }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().id)
    }

    override suspend fun getRecipe(recipeId: Int): ActionStatus<Recipe> {
        val result = handleResponse { api.getRecipe(recipeId) }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().toEntity())
    }

    override suspend fun updateRecipe(recipeId: Int, input: RecipeInput): SimpleAction =
        handleResponse { api.updateRecipe(recipeId, input.toSerializable()) }.toActionStatus().asEmpty()

    override suspend fun deleteRecipe(recipeId: Int): SimpleAction =
        handleResponse { api.deleteRecipe(recipeId) }.toActionStatus().asEmpty()

    companion object {
        const val PAGE_SIZE = 50
    }
}