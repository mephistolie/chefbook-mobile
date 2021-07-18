package com.cactusknights.chefbook.interfaces

import com.cactusknights.chefbook.models.Recipe

interface ContentProvider {

    fun addRecipe(recipe: Recipe, callback: (isAdded: Boolean) -> Unit)
    fun updateRecipe(recipe: Recipe, callback: (isAdded: Boolean) -> Unit)
    fun deleteRecipe(recipe: Recipe, callback: (isDeleted: Boolean) -> Unit)
    fun setRecipeFavoriteStatus(recipe: Recipe)

    fun addToShoppingList(items: ArrayList<String>)
}