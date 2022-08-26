package com.cactusknights.chefbook.domain.usecases.category

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ICreateCategoryUseCase {
    suspend operator fun invoke(input: CategoryInput): Flow<ActionStatus<Category>>
}

class CreateCategoryUseCase @Inject constructor(
    private val repo: ICategoryRepo,
) : ICreateCategoryUseCase {

    override suspend operator fun invoke(input: CategoryInput): Flow<ActionStatus<Category>> = flow {
        emit(Loading)
        emit(repo.createCategory(input))
    }

}
