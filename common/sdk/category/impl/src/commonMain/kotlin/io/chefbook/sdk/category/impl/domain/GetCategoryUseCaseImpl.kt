package io.chefbook.sdk.category.impl.domain

import io.chefbook.sdk.category.api.external.domain.usecases.GetCategoryUseCase
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository


internal class GetCategoryUseCaseImpl(
  private val repo: CategoryRepository,
) : GetCategoryUseCase {

  override suspend operator fun invoke(categoryId: String) = repo.getCategory(categoryId)
}
