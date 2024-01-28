package io.chefbook.sdk.shoppinglist.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase

interface UpdatePurchaseUseCase {
  suspend operator fun invoke(shoppingListId: String, purchase: Purchase): EmptyResult
}
