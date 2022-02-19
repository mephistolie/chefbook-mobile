package com.cactusknights.chefbook.data.sources.remote.datasources

import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.data.ShoppingListDataSource
import com.cactusknights.chefbook.data.sources.remote.api.ShoppingListApi
import com.cactusknights.chefbook.data.sources.remote.dto.toPurchaseDto
import com.cactusknights.chefbook.data.sources.remote.dto.toShoppingList
import com.cactusknights.chefbook.data.sources.remote.dto.toShoppingListInputDto
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class RemoteShoppingListDataSourceImpl @Inject constructor(
    private val api: ShoppingListApi
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