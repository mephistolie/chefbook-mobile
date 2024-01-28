package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.RemovePurchasedItemsUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class RemovePurchasedItemsUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : RemovePurchasedItemsUseCase {

  override suspend operator fun invoke(shoppingListId: String) =
    shoppingListRepository.removePurchasedItems(shoppingListId)
}
