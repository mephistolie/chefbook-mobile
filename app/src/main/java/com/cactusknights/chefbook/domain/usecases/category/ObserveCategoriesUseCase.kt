package com.cactusknights.chefbook.domain.usecases.category

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

interface IObserveCategoriesUseCase {
    suspend operator fun invoke(): StateFlow<List<Category>?>
}

class ObserveCategoriesUseCase @Inject constructor(
    private val categoryRepo: ICategoryRepo,
) : IObserveCategoriesUseCase {

    override suspend operator fun invoke() = categoryRepo.observeCategories()

}
