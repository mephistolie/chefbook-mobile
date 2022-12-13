package com.cactusknights.chefbook.data.datasources.local

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.ShoppingListProto
import com.cactusknights.chefbook.core.toEntity
import com.cactusknights.chefbook.core.toProto
import com.cactusknights.chefbook.data.IShoppingListSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

@Singleton
class LocalShoppingListSource @Inject constructor(
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
