package com.mysty.chefbook.api.category.domain.usecases

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.entities.Category

interface IGetCategoriesUseCase {
    suspend operator fun invoke(): List<Category>
}

internal class GetCategoriesUseCase(
    private val categoriesRepo: ICategoryRepo,
) : IGetCategoriesUseCase {

    override suspend operator fun invoke() = categoriesRepo.getCategories()
        .sortedBy { it.name }

}
