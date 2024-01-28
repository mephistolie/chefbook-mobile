package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.SwitchPurchaseStatusUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class SwitchPurchaseStatusUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : SwitchPurchaseStatusUseCase {

  override suspend operator fun invoke(shoppingListId: String, purchaseId: String) =
    shoppingListRepository.switchPurchaseStatus(shoppingListId, purchaseId)
}
