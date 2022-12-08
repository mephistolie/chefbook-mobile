package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IRecipeInteractionRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeSaveStatusUseCase {
    suspend operator fun invoke(recipeId: Int, saved: Boolean): Flow<SimpleAction>
}

class SetRecipeSaveStatusUseCase @Inject constructor(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeSaveStatusUseCase {

    override suspend operator fun invoke(recipeId: Int, saved: Boolean): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeSavedStatus(recipeId, saved))
    }

}
