package io.chefbook.sdk.shoppinglist.api.external.domain.usecases

interface CreatePurchaseUseCase {
  suspend operator fun invoke(shoppingListId: String): Result<String>
}
