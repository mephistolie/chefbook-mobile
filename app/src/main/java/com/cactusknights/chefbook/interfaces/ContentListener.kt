package com.cactusknights.chefbook.interfaces

import androidx.lifecycle.MutableLiveData
import com.cactusknights.chefbook.models.Recipe

interface ContentListener {

    fun getRecipes(): MutableLiveData<ArrayList<Recipe>>
    fun getRecipesCount(): Int
    fun getCategories(): MutableLiveData<MutableSet<String>>
    fun getShoppingList(): MutableLiveData<ArrayList<String>>
    fun setShoppingList(shoppingList: MutableLiveData<ArrayList<String>>)

    fun listenToRecipes()
    fun listenToShoppingList()
    fun stopListeningRecipes()
    fun stopListenShoppingList()

}