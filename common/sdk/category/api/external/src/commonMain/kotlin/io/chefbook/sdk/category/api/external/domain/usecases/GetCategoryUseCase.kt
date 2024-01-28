package io.chefbook.sdk.category.api.external.domain.usecases

import io.chefbook.sdk.category.api.external.domain.entities.Category

interface GetCategoryUseCase {
  suspend operator fun invoke(categoryId: String): Result<Category>
}
