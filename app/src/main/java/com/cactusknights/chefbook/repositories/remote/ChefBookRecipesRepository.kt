package com.cactusknights.chefbook.repositories.remote

import android.content.SharedPreferences
import android.util.Log
import com.cactusknights.chefbook.domain.RecipesProvider
import com.cactusknights.chefbook.models.BaseRecipe
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.toRecipe
import java.io.IOException
import javax.inject.Inject

class ChefBookRecipesRepository @Inject constructor(
    private val api: ChefBookApi,
    private val preferences: SharedPreferences
) : RecipesProvider {
    override suspend fun getRecipes(): List<BaseRecipe> {
        val response = api.getRecipes()
        Log.e("RESPONSE", response.body().toString())
        val recipes = response.body()
        if (recipes != null) {
            return recipes.map { it.toRecipe() }
        } else throw IOException()
    }

    override suspend fun addRecipe(recipe: BaseRecipe) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecipe(recipe: BaseRecipe) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(recipeId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun setRecipeFavoriteStatus(recipe: BaseRecipe) {
        TODO("Not yet implemented")
    }

    override suspend fun addToShoppingList(items: ArrayList<String>) {
        TODO("Not yet implemented")
    }
}