package io.chefbook.sdk.shoppinglist.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase

interface AddToShoppingListUseCase {
  suspend operator fun invoke(
    shoppingListId: String? = null,
    purchase: Purchase,
    recipeNames: Map<String, String> = emptyMap(),
  ): EmptyResult

  suspend operator fun invoke(
    shoppingListId: String? = null,
    purchases: List<Purchase>,
    recipeNames: Map<String, String> = emptyMap(),
  ): EmptyResult
}
