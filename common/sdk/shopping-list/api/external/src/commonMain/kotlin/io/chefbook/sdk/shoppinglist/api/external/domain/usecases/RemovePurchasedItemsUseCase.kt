package io.chefbook.sdk.shoppinglist.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface RemovePurchasedItemsUseCase {
  suspend operator fun invoke(shoppingListId: String): EmptyResult
}
