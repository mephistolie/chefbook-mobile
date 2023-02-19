package com.mysty.chefbook.api.category.domain.usecases

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.Loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IUpdateCategoryUseCase {
    suspend operator fun invoke(categoryId: String, input: CategoryInput): Flow<ActionStatus<Category>>
}

internal class UpdateCategoryUseCase(
    private val repo: ICategoryRepo,
) : IUpdateCategoryUseCase {

    override suspend operator fun invoke(categoryId: String, input: CategoryInput): Flow<ActionStatus<Category>> = flow {
        emit(Loading)
        emit(repo.updateCategory(categoryId, input))
    }

}
