package com.cactusknights.chefbook.data.sources.local.datasources

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.ShoppingListProto
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.models.toProto
import com.cactusknights.chefbook.models.toShoppingList
import com.cactusknights.chefbook.data.ShoppingListDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalShoppingListDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<ShoppingListProto>,
): ShoppingListDataSource {

    private val saved get() = dataStore.data.take(1)

    override suspend fun getShoppingList() = saved.first().toShoppingList()

    override suspend fun setShoppingList(shoppingList: ShoppingList) {
        shoppingList.timestamp = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis)
        dataStore.updateData { shoppingList.toProto() }
    }

    override suspend fun addToShoppingList(purchases: List<Purchase>) {
        var newPurchases = purchases
        val shoppingList = getShoppingList()
        shoppingList.timestamp = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis)
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