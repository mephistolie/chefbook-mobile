package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IRecipeInteractionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeCategoriesUseCase {
    suspend operator fun invoke(recipeId: String, categories: List<String>): Flow<SimpleAction>
}

class SetRecipeCategoriesUseCase(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeCategoriesUseCase {

    override suspend operator fun invoke(recipeId: String, categories: List<String>): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeCategories(recipeId, categories))
    }

}
