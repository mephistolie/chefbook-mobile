package io.chefbook.sdk.shoppinglist.impl.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.GetShoppingListsUseCase
import io.chefbook.sdk.shoppinglist.api.internal.data.repositories.ShoppingListRepository

internal class GetShoppingListsUseCaseImpl(
  private val shoppingListRepository: ShoppingListRepository,
) : GetShoppingListsUseCase {

  override suspend fun invoke(): Result<List<ShoppingListMeta>> =
    shoppingListRepository.getShoppingLists()
}
