package io.chefbook.sdk.shoppinglist.impl.data.sources.remote

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.impl.data.sources.ShoppingListDataSource

internal interface RemoteShoppingListDataSource : ShoppingListDataSource {

  suspend fun addToShoppingList(
    shoppingListId: String,
    purchases: List<Purchase>,
  ): Result<Int>
}
