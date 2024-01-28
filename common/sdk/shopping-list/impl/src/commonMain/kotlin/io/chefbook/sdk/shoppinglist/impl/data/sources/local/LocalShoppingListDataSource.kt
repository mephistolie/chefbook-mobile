package io.chefbook.sdk.shoppinglist.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import io.chefbook.sdk.shoppinglist.impl.data.sources.ShoppingListDataSource
import kotlinx.coroutines.flow.Flow

internal interface LocalShoppingListDataSource : ShoppingListDataSource {

  fun observeShoppingLists(): Flow<List<ShoppingListMeta>>

  fun observeShoppingList(shoppingListId: String): Flow<ShoppingList?>

  suspend fun updateShoppingList(shoppingListId: String, update: (ShoppingList) -> ShoppingList): EmptyResult

  suspend fun setShoppingListVersion(shoppingListId: String, version: Int): EmptyResult

  suspend fun addToShoppingList(
    shoppingListId: String,
    purchases: List<Purchase>,
    recipeNames: Map<String, String>,
  ): Result<Int>

  suspend fun clear()
}
