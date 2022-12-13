package com.cactusknights.chefbook.domain.usecases.category

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IGetCategoryUseCase {
    suspend operator fun invoke(categoryId: String): Flow<ActionStatus<Category>>
}

class GetCategoryUseCase @Inject constructor(
    private val repo: ICategoryRepo,
) : IGetCategoryUseCase {

    override suspend operator fun invoke(categoryId: String): Flow<ActionStatus<Category>> = flow {
        emit(Loading)
        emit(repo.getCategory(categoryId))
    }

}
