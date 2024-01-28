package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.AddToShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class AddToShoppingListUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : AddToShoppingListUseCase {

  override suspend fun invoke(
    shoppingListId: String?,
    purchase: Purchase,
    recipeNames: Map<String, String>,
  ) =
    invoke(shoppingListId, listOf(purchase))

  override suspend fun invoke(
    shoppingListId: String?,
    purchases: List<Purchase>,
    recipeNames: Map<String, String>,
  ) =
    shoppingListRepository.addToShoppingList(shoppingListId, purchases, recipeNames)
}
