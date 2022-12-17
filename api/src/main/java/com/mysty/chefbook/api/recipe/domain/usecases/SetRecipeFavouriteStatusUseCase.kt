package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeFavouriteStatusUseCase {
    suspend operator fun invoke(recipeId: String, favourite: Boolean): Flow<SimpleAction>
}

internal class SetRecipeFavouriteStatusUseCase(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeFavouriteStatusUseCase {

    override suspend operator fun invoke(recipeId: String, favourite: Boolean): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeFavouriteStatus(recipeId, favourite))
    }

}
