package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.UpdatePurchaseUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class UpdatePurchaseUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : UpdatePurchaseUseCase {

  override suspend operator fun invoke(shoppingListId: String, purchase: Purchase) =
    shoppingListRepository.updatePurchase(shoppingListId, purchase)
}
