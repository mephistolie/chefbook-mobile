package com.mysty.chefbook.api.recipe.data.remote

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.common.network.dto.isSuccess
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import com.mysty.chefbook.api.recipe.data.remote.api.RecipeApi
import com.mysty.chefbook.api.recipe.data.remote.dto.toEntity
import com.mysty.chefbook.api.recipe.data.remote.dto.toSerializable
import com.mysty.chefbook.api.recipe.data.repositories.IRemoteRecipeSource
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.RecipesFilter
import java.util.UUID

internal class RemoteRecipeSource(
    private val api: RecipeApi,
    private val handleResponse: INetworkHandler,
) : IRemoteRecipeSource {

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
                languages = query.languages?.map { it.code }
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

    override suspend fun createRecipe(input: RecipeInput): ActionStatus<String> {
        val result = handleResponse { api.createRecipe(input.toSerializable(recipeId = UUID.randomUUID().toString())) }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().id)
    }

    override suspend fun getRecipe(recipeId: String): ActionStatus<Recipe> {
        val result = handleResponse { api.getRecipe(recipeId) }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().toEntity())
    }

    override suspend fun updateRecipe(recipeId: String, input: RecipeInput): SimpleAction =
        handleResponse { api.updateRecipe(recipeId, input.toSerializable()) }.toActionStatus().asEmpty()

    override suspend fun deleteRecipe(recipeId: String): SimpleAction =
        handleResponse { api.deleteRecipe(recipeId) }.toActionStatus().asEmpty()

    companion object {
        const val PAGE_SIZE = 50
    }
}