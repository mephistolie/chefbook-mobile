package com.cactusknights.chefbook.domain.usecases.category

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import javax.inject.Inject

interface IGetCategoriesUseCase {
    suspend operator fun invoke(): List<Category>
}

class GetCategoriesUseCase @Inject constructor(
    private val categoriesRepo: ICategoryRepo,
) : IGetCategoriesUseCase {

    override suspend operator fun invoke() = categoriesRepo.getCategories()
        .sortedBy { it.name }

}
