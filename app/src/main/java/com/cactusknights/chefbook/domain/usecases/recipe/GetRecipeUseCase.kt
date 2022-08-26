package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IGetRecipeUseCase {
    suspend operator fun invoke(recipeId: Int): Flow<ActionStatus<Recipe>>
}

class GetRecipeUseCase @Inject constructor(
    private val recipeRepo: IRecipeRepo,
    private val latestRecipesRepo: ILatestRecipesRepo,
) : IGetRecipeUseCase {

    override suspend operator fun invoke(recipeId: Int): Flow<ActionStatus<Recipe>> = flow {
        emit(Loading)
        val result = recipeRepo.getRecipe(recipeId)
        if (result.isSuccess()) latestRecipesRepo.pushRecipe(recipeId)
        emit(result)
    }

}
