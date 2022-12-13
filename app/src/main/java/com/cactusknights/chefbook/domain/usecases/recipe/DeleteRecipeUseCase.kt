package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IDeleteRecipeUseCase {
    suspend operator fun invoke(recipeId: String): Flow<SimpleAction>
}

class DeleteRecipeUseCase @Inject constructor(
    private val recipeRepo: IRecipeRepo,
) : IDeleteRecipeUseCase {

    override suspend operator fun invoke(recipeId: String): Flow<SimpleAction> = flow {
        emit(Loading)
        emit(recipeRepo.deleteRecipe(recipeId))
    }

}
