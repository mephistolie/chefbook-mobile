package com.cactusknights.chefbook.repositories.remote.datasources

import android.util.Log
import com.cactusknights.chefbook.domain.ShoppingListDataSource
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.dto.*
import com.google.gson.Gson
import java.io.IOException

class RemoteShoppingListDataSource(
    private val api: ChefBookApi
) : ShoppingListDataSource {

    override suspend fun getShoppingList(): ShoppingList {
        val response = api.getShoppingList()
        val shoppingList = response.body()
        if (shoppingList != null) return shoppingList.toShoppingList() else throw IOException()
    }

    override suspend fun setShoppingList(shoppingList: ShoppingList) {
        api.setShoppingList(shoppingList.toShoppingListInputDto())
    }

    override suspend fun addToShoppingList(purchases: List<Purchase>) {
        api.addToShoppingList(purchases.map { it.toPurchaseDto() })
    }
}