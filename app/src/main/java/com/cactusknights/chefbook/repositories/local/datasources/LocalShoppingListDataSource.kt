package com.cactusknights.chefbook.repositories.local.datasources

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.ShoppingListProto
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.models.toProto
import com.cactusknights.chefbook.models.toShoppingList
import com.cactusknights.chefbook.repositories.ShoppingListDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalShoppingListDataSource @Inject constructor(
    private val dataStore: DataStore<ShoppingListProto>,
): ShoppingListDataSource {

    private val saved get() = dataStore.data.take(1)

    override suspend fun getShoppingList() = saved.first().toShoppingList()

    override suspend fun setShoppingList(shoppingList: ShoppingList) {
        dataStore.updateData { shoppingList.toProto() }
    }

    override suspend fun addToShoppingList(purchases: List<Purchase>) {
        var newPurchases = purchases
        val shoppingList = getShoppingList()
        shoppingList.timestamp = Date()
        shoppingList.purchases.map { purchase ->
            val count = newPurchases.filter { it.name == purchase.name }.size; purchase.multiplier += count
            newPurchases = newPurchases.filter { it.name != purchase.name } as ArrayList<Purchase>
        }
        val newSortedPurchases = arrayListOf<Purchase>(); newSortedPurchases.addAll(shoppingList.purchases)
        newSortedPurchases.addAll(newPurchases)
        val newShoppingList = ShoppingList(newSortedPurchases)
        setShoppingList(newShoppingList)
    }
}