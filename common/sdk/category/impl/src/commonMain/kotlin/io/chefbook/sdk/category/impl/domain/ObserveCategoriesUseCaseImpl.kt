package io.chefbook.sdk.category.impl.domain

import io.chefbook.sdk.category.api.external.domain.usecases.ObserveCategoriesUseCase
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository
import kotlinx.coroutines.flow.onStart


internal class ObserveCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository,
) : ObserveCategoriesUseCase {

    override operator fun invoke() = categoryRepository.observeCategories()
        .onStart { emit(null) }
}
