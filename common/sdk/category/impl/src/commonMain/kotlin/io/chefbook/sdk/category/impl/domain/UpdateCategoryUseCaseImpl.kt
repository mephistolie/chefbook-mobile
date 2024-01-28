package io.chefbook.sdk.category.impl.domain

import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.category.api.external.domain.usecases.UpdateCategoryUseCase
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository

internal class UpdateCategoryUseCaseImpl(
  private val repo: CategoryRepository,
) : UpdateCategoryUseCase {

  override suspend operator fun invoke(categoryId: String, input: CategoryInput) =
    repo.updateCategory(categoryId, input)
}
