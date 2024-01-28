package io.chefbook.sdk.category.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface DeleteCategoryUseCase {
  suspend operator fun invoke(categoryId: String): EmptyResult
}