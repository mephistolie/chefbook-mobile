package com.cactusknights.chefbook.domain

import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable

interface RecipesProvider {

    suspend fun getRecipes(): List<Recipe>

    suspend fun addRecipe(recipe: Recipe)
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipeId: Int)
    suspend fun setRecipeFavoriteStatus(recipe: Recipe)

    suspend fun addToShoppingList(items: ArrayList<String>)
}