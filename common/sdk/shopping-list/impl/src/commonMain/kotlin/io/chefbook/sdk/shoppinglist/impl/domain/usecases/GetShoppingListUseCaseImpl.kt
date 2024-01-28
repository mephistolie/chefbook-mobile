package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.GetShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class GetShoppingListUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : GetShoppingListUseCase {

  override suspend operator fun invoke(shoppingListId: String) =
    shoppingListRepository.getShoppingList(shoppingListId)
}
