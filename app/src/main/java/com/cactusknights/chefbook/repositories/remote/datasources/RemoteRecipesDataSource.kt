package com.cactusknights.chefbook.repositories.remote.datasources

import android.util.Log
import com.cactusknights.chefbook.domain.RecipesDataSource
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.RecipeFavouriteInputDto
import com.cactusknights.chefbook.repositories.remote.dto.toRecipe
import com.cactusknights.chefbook.repositories.remote.dto.toRecipeInputDto
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class RemoteRecipesDataSource @Inject constructor(
    private val api: ChefBookApi
) : RecipesDataSource {
    override suspend fun getRecipes(): ArrayList<Recipe> {
        val response = api.getRecipes()
        val recipeDtos = response.body()
        if (recipeDtos != null) {
            val recipes: ArrayList<Recipe> = arrayListOf()
            return recipeDtos.map { it.toRecipe() }.toCollection(recipes)
        } else throw IOException()
    }

    override suspend fun addRecipe(recipe: Recipe) : Int {
        Log.e("JSON", Gson().toJson(recipe.toRecipeInputDto()))
        val response = api.createRecipe(recipe.toRecipeInputDto())
        response.body()
        return response.body()!!.id
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        api.updateRecipe(recipe.remoteId.toString(), recipe.toRecipeInputDto())
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        api.deleteRecipe(recipe.remoteId!!.toString())
    }

    override suspend fun setRecipeFavouriteStatus(recipe: Recipe) {
        api.markRecipeFavourite(RecipeFavouriteInputDto(recipe.remoteId!!, recipe.isFavourite))
    }
}