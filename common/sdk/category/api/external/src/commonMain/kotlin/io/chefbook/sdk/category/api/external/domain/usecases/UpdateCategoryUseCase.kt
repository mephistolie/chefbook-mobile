package io.chefbook.sdk.category.api.external.domain.usecases

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput

interface UpdateCategoryUseCase {
  suspend operator fun invoke(categoryId: String, input: CategoryInput): Result<Category>
}
