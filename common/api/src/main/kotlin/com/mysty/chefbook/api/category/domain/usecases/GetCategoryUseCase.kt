package com.mysty.chefbook.api.category.domain.usecases

import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.Loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IGetCategoryUseCase {
    suspend operator fun invoke(categoryId: String): Flow<ActionStatus<Category>>
}

internal class GetCategoryUseCase(
    private val repo: ICategoryRepo,
) : IGetCategoryUseCase {

    override suspend operator fun invoke(categoryId: String): Flow<ActionStatus<Category>> = flow {
        emit(Loading)
        emit(repo.getCategory(categoryId))
    }

}
