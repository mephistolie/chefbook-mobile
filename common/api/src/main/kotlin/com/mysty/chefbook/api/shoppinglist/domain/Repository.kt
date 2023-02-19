package com.mysty.chefbook.api.shoppinglist.domain

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList
import kotlinx.coroutines.flow.StateFlow

internal interface IShoppingListRepo {
    suspend fun observeShoppingList(): StateFlow<ShoppingList>
    suspend fun getShoppingList(forceRefresh: Boolean = false): ActionStatus<ShoppingList>
    suspend fun refreshShoppingList()
    suspend fun syncShoppingList()
    suspend fun setShoppingList(purchases: List<Purchase>): SimpleAction
    suspend fun createPurchase() : ActionStatus<String>
    suspend fun updatePurchase(purchase: Purchase) : SimpleAction
    suspend fun switchPurchaseStatus(purchaseId: String): SimpleAction
    suspend fun removePurchasedItems(): SimpleAction
    suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction
}
