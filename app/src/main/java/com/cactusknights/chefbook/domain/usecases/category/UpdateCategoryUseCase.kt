package com.cactusknights.chefbook.domain.usecases.category

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IUpdateCategoryUseCase {
    suspend operator fun invoke(categoryId: String, input: CategoryInput): Flow<ActionStatus<Category>>
}

class UpdateCategoryUseCase @Inject constructor(
    private val repo: ICategoryRepo,
) : IUpdateCategoryUseCase {

    override suspend operator fun invoke(categoryId: String, input: CategoryInput): Flow<ActionStatus<Category>> = flow {
        emit(Loading)
        emit(repo.updateCategory(categoryId, input))
    }

}
