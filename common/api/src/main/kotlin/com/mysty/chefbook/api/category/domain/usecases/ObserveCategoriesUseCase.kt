package com.mysty.chefbook.api.category.domain.usecases

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.entities.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

interface IObserveCategoriesUseCase {
    operator fun invoke(): Flow<List<Category>?>
}

internal class ObserveCategoriesUseCase(
    private val categoryRepo: ICategoryRepo,
) : IObserveCategoriesUseCase {

    override operator fun invoke() = categoryRepo.observeCategories()
        .onStart { emit(null) }
}
