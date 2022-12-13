package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IRecipeInteractionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeFavouriteStatusUseCase {
    suspend operator fun invoke(recipeId: String, favourite: Boolean): Flow<SimpleAction>
}

class SetRecipeFavouriteStatusUseCase(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeFavouriteStatusUseCase {

    override suspend operator fun invoke(recipeId: String, favourite: Boolean): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeFavouriteStatus(recipeId, favourite))
    }

}
