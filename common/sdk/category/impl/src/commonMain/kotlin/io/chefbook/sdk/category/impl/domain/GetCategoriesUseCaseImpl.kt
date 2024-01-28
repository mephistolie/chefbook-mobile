package io.chefbook.sdk.category.impl.domain

import io.chefbook.sdk.category.api.external.domain.usecases.GetCategoriesUseCase
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository

internal class GetCategoriesUseCaseImpl(
  private val categoriesRepository: CategoryRepository,
) : GetCategoriesUseCase {

  override suspend operator fun invoke() = categoriesRepository.getCategories()
    .sortedBy { it.name }
}
