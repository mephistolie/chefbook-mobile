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
        val shoppingListEntity = ShoppingList(shoppingList, LocalDateTime.now())
        dataStore.updateData { shoppingListEntity.toProto() }
        return SuccessResult
    }

    override suspend fun addToShoppingList(purchases: List<Purchase>): SimpleAction {
        var newPurchases = purchases
        val shoppingList = getShoppingList().data()
        val updatedPurchases = shoppingList.purchases.map { purchase ->
            val count = newPurchases.filter { it.name == purchase.name }.size
            val updatedPurchase = purchase.copy(multiplier = purchase.multiplier + count)
            newPurchases = newPurchases.filter { it.name != purchase.name } as ArrayList<Purchase>
            updatedPurchase
        }
        val newUniquePurchases = arrayListOf<Purchase>()
        newUniquePurchases.addAll(updatedPurchases)
        newUniquePurchases.addAll(newPurchases)

        return setShoppingList(newUniquePurchases)
    }

}
