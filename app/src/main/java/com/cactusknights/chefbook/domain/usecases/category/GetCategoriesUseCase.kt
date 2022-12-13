package com.cactusknights.chefbook.domain.usecases.category

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo

interface IGetCategoriesUseCase {
    suspend operator fun invoke(): List<Category>
}

class GetCategoriesUseCase(
    private val categoriesRepo: ICategoryRepo,
) : IGetCategoriesUseCase {

    override suspend operator fun invoke() = categoriesRepo.getCategories()
        .sortedBy { it.name }

}
