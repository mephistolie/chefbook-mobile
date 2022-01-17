package com.cactusknights.chefbook.repositories.local.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cactusknights.chefbook.domain.ShoppingListDataSource
import com.cactusknights.chefbook.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LocalShoppingListDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    val gson: Gson
): ShoppingListDataSource {

    companion object {
        private val SHOPPING_LIST = stringPreferencesKey("shopping_list")
        const val MINIMAL_DATE: Long = 1000
        const val EMPTY_SHOPPING_LIST = "{}"
    }

    override suspend fun getShoppingList(): ShoppingList {
        val type: Type = object : TypeToken<ShoppingList?>() {}.type
        val prefs = dataStore.data.first()
        val shoppingListString = prefs[SHOPPING_LIST]?: EMPTY_SHOPPING_LIST
        if (shoppingListString == EMPTY_SHOPPING_LIST) return ShoppingList(purchases = listOf(), timestamp = Date(MINIMAL_DATE))
        val shoppingList : ShoppingList = gson.fromJson(prefs[SHOPPING_LIST]?: "{}", type)
        return if (shoppingList.purchases != null && shoppingList.timestamp != null) shoppingList else ShoppingList(arrayListOf())
    }

    override suspend fun setShoppingList(shoppingList: ShoppingList) {
        shoppingList.timestamp = Date()
        dataStore.edit { prefs -> prefs[SHOPPING_LIST] = gson.toJson(shoppingList) }
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