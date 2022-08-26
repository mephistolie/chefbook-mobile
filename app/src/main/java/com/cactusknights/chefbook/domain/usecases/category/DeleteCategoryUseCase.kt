package com.cactusknights.chefbook.domain.usecases.category

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IDeleteCategoryUseCase {
    suspend operator fun invoke(categoryId: Int): Flow<ActionStatus<Category>>
}

class DeleteCategoryUseCase @Inject constructor(
    private val repo: ICategoryRepo,
) : IDeleteCategoryUseCase {

    override suspend operator fun invoke(categoryId: Int): Flow<ActionStatus<Category>> = flow {
        emit(Loading)
        emit(repo.deleteCategory(categoryId))
    }

}
