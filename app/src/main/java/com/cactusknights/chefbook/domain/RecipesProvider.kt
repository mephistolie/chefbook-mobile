package com.cactusknights.chefbook.domain

import com.cactusknights.chefbook.models.BaseRecipe
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable

interface RecipesProvider {

    suspend fun getRecipes(): List<BaseRecipe>

    suspend fun addRecipe(recipe: BaseRecipe)
    suspend fun updateRecipe(recipe: BaseRecipe)
    suspend fun deleteRecipe(recipeId: Int)
    suspend fun setRecipeFavoriteStatus(recipe: BaseRecipe)

    suspend fun addToShoppingList(items: ArrayList<String>)
}