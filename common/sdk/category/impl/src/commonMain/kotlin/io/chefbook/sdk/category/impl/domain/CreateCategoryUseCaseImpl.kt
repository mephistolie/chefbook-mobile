package io.chefbook.sdk.category.impl.domain

import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.category.api.external.domain.usecases.CreateCategoryUseCase
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository

internal class CreateCategoryUseCaseImpl(
  private val repo: CategoryRepository,
) : CreateCategoryUseCase {

  override suspend operator fun invoke(input: CategoryInput) = repo.createCategory(input)
}
