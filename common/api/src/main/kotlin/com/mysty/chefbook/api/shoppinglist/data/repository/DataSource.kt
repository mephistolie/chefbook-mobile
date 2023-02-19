package com.mysty.chefbook.api.shoppinglist.data.repository

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList


internal interface IShoppingListSource {
    suspend fun getShoppingList(): ActionStatus<ShoppingList>
    suspend fun setShoppingList(shoppingList: List<Purchase>): SimpleAction
    suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction
}
