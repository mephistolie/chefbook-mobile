package io.chefbook.sdk.shoppinglist.api.external.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList

interface GetShoppingListUseCase {
  suspend operator fun invoke(shoppingListId: String): Result<ShoppingList>
}
