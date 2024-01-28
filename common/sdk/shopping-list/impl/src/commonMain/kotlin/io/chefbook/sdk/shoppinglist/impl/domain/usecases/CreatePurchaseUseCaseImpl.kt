package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.CreatePurchaseUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class CreatePurchaseUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : CreatePurchaseUseCase {

  override suspend operator fun invoke(shoppingListId: String) =
    shoppingListRepository.createPurchase(shoppingListId)
}
