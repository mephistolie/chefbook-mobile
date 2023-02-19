package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Loading
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.recipe.domain.ILatestRecipesRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IGetRecipeUseCase {
    suspend operator fun invoke(recipeId: String): Flow<ActionStatus<Recipe>>
}

internal class GetRecipeUseCase(
    private val recipeRepo: IRecipeRepo,
    private val latestRecipesRepo: ILatestRecipesRepo,
) : IGetRecipeUseCase {

    override suspend operator fun invoke(recipeId: String): Flow<ActionStatus<Recipe>> = flow {
        emit(Loading)

        val result = recipeRepo.getRecipe(recipeId)
        if (result.isFailure()) {
            emit(result)
            return@flow
        }

        latestRecipesRepo.pushRecipe(recipeId)
        emit(DataResult(result.data()))
    }

}
