package com.cactusknights.chefbook.data.sources.remote.datasources.recipes

import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo
import com.cactusknights.chefbook.data.RecipeCrudDataSource
import com.cactusknights.chefbook.data.sources.remote.api.RecipesApi
import com.cactusknights.chefbook.data.sources.remote.dto.toRecipe
import com.cactusknights.chefbook.data.sources.remote.dto.info
import com.cactusknights.chefbook.data.sources.remote.dto.toRecipeInputDto
import java.io.IOException
import java.util.*
import javax.inject.Inject

class RemoteRecipeBookDataSourceImpl @Inject constructor(
    private val api: RecipesApi
) : RecipeCrudDataSource {

    override suspend fun getRecipeBook(): List<RecipeInfo> {
        val recipesInfo = arrayListOf<RecipeInfo>()
        var page = 1
        var isEnd = false
        while (!isEnd) {
            val response = api.getRecipes(owned = true, page = page, pageSize = PAGE_SIZE)
            val recipeDtos = response.body()
            if (!recipeDtos.isNullOrEmpty()) {
                recipesInfo.addAll(recipeDtos.map { it.info() })
                if (recipeDtos.size < PAGE_SIZE) isEnd = true
                page++
            }
            else isEnd = true
        }
        return recipesInfo
    }

    override suspend fun createRecipe(recipe: Recipe): Recipe {
        val response = api.createRecipe(recipe.toRecipeInputDto())
        recipe.remoteId = response.body()!!.id
        recipe.creationTimestamp = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis)
        recipe.updateTimestamp = recipe.creationTimestamp
        recipe.userTimestamp = recipe.creationTimestamp
        return recipe
    }

    override suspend fun getRecipeById(recipeId: Int): Recipe {
        val response = api.getRecipe(recipeId.toString())
        val recipeDto = response.body()
        if (recipeDto != null) return recipeDto.toRecipe()
        else throw IOException()
    }

    override suspend fun updateRecipe(recipe: Recipe): Recipe {
        api.updateRecipe(recipe.remoteId.toString(), recipe.toRecipeInputDto())
        return recipe
    }

    override suspend fun deleteRecipe(recipeId: Int) {
        api.deleteRecipe(recipeId.toString())
    }

    companion object {
        const val PAGE_SIZE = 50
    }
}