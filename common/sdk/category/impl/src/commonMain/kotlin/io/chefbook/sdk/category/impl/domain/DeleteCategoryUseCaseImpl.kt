package io.chefbook.sdk.category.impl.domain

import io.chefbook.sdk.category.api.external.domain.usecases.DeleteCategoryUseCase
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository

internal class DeleteCategoryUseCaseImpl(
  private val repo: CategoryRepository,
) : DeleteCategoryUseCase {

  override suspend operator fun invoke(categoryId: String) = repo.deleteCategory(categoryId)
}
