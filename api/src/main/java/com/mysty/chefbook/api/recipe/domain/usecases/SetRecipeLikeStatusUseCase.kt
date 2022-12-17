package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeLikeStatusUseCase {
    suspend operator fun invoke(recipeId: String, liked: Boolean): Flow<SimpleAction>
}

internal class SetRecipeLikeStatusUseCase(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeLikeStatusUseCase {

    override suspend operator fun invoke(recipeId: String, liked: Boolean): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeLikeStatus(recipeId, liked))
    }

}
