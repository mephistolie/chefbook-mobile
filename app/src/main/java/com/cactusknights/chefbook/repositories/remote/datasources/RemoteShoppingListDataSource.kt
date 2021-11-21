package com.cactusknights.chefbook.repositories.remote.datasources

import com.cactusknights.chefbook.domain.ShoppingListDataSource
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import java.io.IOException

class RemoteShoppingListDataSource(
    private val api: ChefBookApi
) : ShoppingListDataSource {
    override suspend fun getShoppingList(): ArrayList<Selectable<String>> {
        val response = api.getShoppingList()
        val shoppingList = response.body()
        if (shoppingList != null) return shoppingList else throw IOException()
    }

    override suspend fun setShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        api.setShoppingList(shoppingList)
    }

    override suspend fun addToShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        api.addToShoppingList(shoppingList)
    }
}