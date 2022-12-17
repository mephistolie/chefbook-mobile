package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeSaveStatusUseCase {
    suspend operator fun invoke(recipeId: String, saved: Boolean): Flow<SimpleAction>
}

internal class SetRecipeSaveStatusUseCase(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeSaveStatusUseCase {

    override suspend operator fun invoke(recipeId: String, saved: Boolean): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeSavedStatus(recipeId, saved))
    }

}
