package io.chefbook.sdk.shoppinglist.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

  fun observeShoppingLists(): Flow<List<ShoppingListMeta>>

  suspend fun getShoppingLists(): Result<List<ShoppingListMeta>>

  fun observeShoppingList(shoppingListId: String): Flow<ShoppingList?>

  suspend fun getShoppingList(shoppingListId: String? = null): Result<ShoppingList>

  suspend fun createPurchase(shoppingListId: String): Result<String>

  suspend fun updatePurchase(shoppingListId: String, purchase: Purchase): EmptyResult

  suspend fun switchPurchaseStatus(shoppingListId: String, purchaseId: String): EmptyResult

  suspend fun removePurchasedItems(shoppingListId: String): EmptyResult

  suspend fun addToShoppingList(
    shoppingListId: String? = null,
    purchases: List<Purchase>,
    recipeNames: Map<String, String> = emptyMap(),
  ): EmptyResult

  suspend fun refreshShoppingList()

  suspend fun clearLocalData()
}
