package io.chefbook.sdk.shoppinglist.api.external.domain.usecases

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta

interface GetShoppingListsUseCase {
  suspend operator fun invoke(): Result<List<ShoppingListMeta>>
}
