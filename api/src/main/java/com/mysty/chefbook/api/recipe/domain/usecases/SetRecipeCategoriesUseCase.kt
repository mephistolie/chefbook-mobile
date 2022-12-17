package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeCategoriesUseCase {
    suspend operator fun invoke(recipeId: String, categories: List<String>): Flow<SimpleAction>
}

internal class SetRecipeCategoriesUseCase(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeCategoriesUseCase {

    override suspend operator fun invoke(recipeId: String, categories: List<String>): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeCategories(recipeId, categories))
    }

}
