package com.mysty.chefbook.api.category.domain.usecases

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.Loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IDeleteCategoryUseCase {
    suspend operator fun invoke(categoryId: String): Flow<ActionStatus<Category>>
}

internal class DeleteCategoryUseCase(
    private val repo: ICategoryRepo,
) : IDeleteCategoryUseCase {

    override suspend operator fun invoke(categoryId: String): Flow<ActionStatus<Category>> = flow {
        emit(Loading)
        emit(repo.deleteCategory(categoryId))
    }

}
