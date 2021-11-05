package com.cactusknights.chefbook.domain

import com.cactusknights.chefbook.models.Recipe

interface ContentProvider {

    suspend fun addRecipe(recipe: Recipe, callback: (isAdded: Boolean) -> Unit)
    suspend fun updateRecipe(recipe: Recipe, callback: (isAdded: Boolean) -> Unit)
    suspend fun deleteRecipe(recipe: Recipe, callback: (isDeleted: Boolean) -> Unit)
    suspend fun setRecipeFavoriteStatus(recipe: Recipe)

    suspend fun addToShoppingList(items: ArrayList<String>)
}