package io.chefbook.sdk.category.api.external.domain.usecases

import io.chefbook.sdk.category.api.external.domain.entities.Category

interface GetCategoriesUseCase {
  suspend operator fun invoke(): List<Category>
}
