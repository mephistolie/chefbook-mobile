package io.chefbook.sdk.category.api.external.domain.usecases

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput

interface CreateCategoryUseCase {
  suspend operator fun invoke(input: CategoryInput): Result<Category>
}
