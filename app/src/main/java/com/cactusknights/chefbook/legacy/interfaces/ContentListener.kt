package com.cactusknights.chefbook.legacy.interfaces

import com.cactusknights.chefbook.models.Recipe
import kotlinx.coroutines.flow.MutableStateFlow

interface ContentListener {

    suspend fun getRecipes(): MutableStateFlow<ArrayList<Recipe>>
    suspend fun getRecipesCount(): Int
    suspend fun getCategories(): MutableStateFlow<ArrayList<String>>
    suspend fun getShoppingList(): MutableStateFlow<ArrayList<String>>

    suspend fun listenToRecipes()
    suspend fun listenToShoppingList()
    suspend fun stopListeningRecipes()
    suspend fun stopListenShoppingList()
    suspend fun setShoppingList(shoppingList: ArrayList<String>)

}