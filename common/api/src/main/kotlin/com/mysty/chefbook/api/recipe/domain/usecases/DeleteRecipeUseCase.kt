package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IDeleteRecipeUseCase {
    suspend operator fun invoke(recipeId: String): Flow<SimpleAction>
}

internal class DeleteRecipeUseCase(
    private val recipeRepo: IRecipeRepo,
) : IDeleteRecipeUseCase {

    override suspend operator fun invoke(recipeId: String): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.deleteRecipe(recipeId))
    }

}
