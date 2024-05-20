package io.chefbook.sdk.shoppinglist.impl.data.sources

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta

internal interface ShoppingListDataSource {

  suspend fun getShoppingLists(): Result<List<ShoppingListMeta>>

  suspend fun getShoppingList(shoppingListId: String): Result<ShoppingList>

  suspend fun getPersonalShoppingList(): Result<ShoppingList>

  suspend fun setShoppingList(shoppingList: ShoppingList): Result<Int>

  suspend fun deleteShoppingList(shoppingListId: String): EmptyResult
}
