package com.mysty.chefbook.api.shoppinglist.data.local

import androidx.datastore.core.DataStore
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.shoppinglist.data.local.dto.ShoppingListProto
import com.mysty.chefbook.api.shoppinglist.data.local.mappers.toEntity
import com.mysty.chefbook.api.shoppinglist.data.local.mappers.toProto
import com.mysty.chefbook.api.shoppinglist.data.repository.IShoppingListSource
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

internal class LocalShoppingListSource(
    private val dataStore: DataStore<ShoppingListProto>,
): IShoppingListSource {

    private val saved get() = dataStore.data.take(1)

    override suspend fun getShoppingList(): ActionStatus<ShoppingList> =
        DataResult(saved.first().toEntity())

    override suspend fun setShoppingList(shoppingList: List<Purchase>): SimpleAction {
        val shoppingListEntity = ShoppingList(shoppingList, LocalDateTime.now(ZoneOffset.UTC))
        dataStore.updateData { shoppingListEntity.toProto() }
        return SuccessResult
    }

    override suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction {
        val shoppingList = getShoppingList().data()

        val purchasesById = shoppingList.purchases.associateBy({ it.id }, { it }).toMutableMap()
        val purchasesByName = shoppingList.purchases.associateBy({ it.name }, { it }).toMutableMap()

        val updatedPurchases = mutableListOf<Purchase>()

        purchases.forEach { newPurchase ->
            val purchaseById = purchasesById[newPurchase.id]
            val purchaseByName = purchasesByName[newPurchase.name]
            when {
                purchaseById != null && newPurchase.amount != null && newPurchase.amount > 0 -> {
                    updatedPurchases.add(purchaseById.copy(amount = (purchaseById.amount ?: 0) + newPurchase.amount))
                }
                purchaseByName != null -> {
                    updatedPurchases.add(purchaseByName.copy(multiplier = purchaseByName.multiplier + 1))
                }
                else -> updatedPurchases.add(newPurchase)
            }
            purchasesById.remove(newPurchase.id)
        }
        updatedPurchases.addAll(purchasesById.values)

        return setShoppingList(updatedPurchases)
    }

}
