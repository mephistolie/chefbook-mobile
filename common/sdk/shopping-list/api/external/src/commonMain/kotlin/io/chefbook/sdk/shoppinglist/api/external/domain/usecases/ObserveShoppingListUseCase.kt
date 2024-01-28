package io.chefbook.sdk.shoppinglist.api.external.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ObserveShoppingListUseCase {
  operator fun invoke(shoppingListId: String): Flow<ShoppingList?>
}
