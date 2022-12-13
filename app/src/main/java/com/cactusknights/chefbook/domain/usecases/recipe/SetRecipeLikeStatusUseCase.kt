package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IRecipeInteractionRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ISetRecipeLikeStatusUseCase {
    suspend operator fun invoke(recipeId: String, liked: Boolean): Flow<SimpleAction>
}

class SetRecipeLikeStatusUseCase @Inject constructor(
    private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeLikeStatusUseCase {

    override suspend operator fun invoke(recipeId: String, liked: Boolean): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.setRecipeLikeStatus(recipeId, liked))
    }

}
