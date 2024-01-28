package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.ObserveShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class ObserveShoppingListUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : ObserveShoppingListUseCase {

  override operator fun invoke(shoppingListId: String) =
    shoppingListRepository.observeShoppingList(shoppingListId)
}
