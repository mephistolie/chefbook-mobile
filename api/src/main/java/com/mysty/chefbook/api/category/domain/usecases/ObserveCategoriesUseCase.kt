package com.mysty.chefbook.api.category.domain.usecases

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.entities.Category
import kotlinx.coroutines.flow.StateFlow

interface IObserveCategoriesUseCase {
    suspend operator fun invoke(): StateFlow<List<Category>?>
}

internal class ObserveCategoriesUseCase(
    private val categoryRepo: ICategoryRepo,
) : IObserveCategoriesUseCase {

    override suspend operator fun invoke() = categoryRepo.observeCategories()

}
