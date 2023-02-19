package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.recipe.domain.ILatestRecipesRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IObserveRecipeUseCase {
    suspend operator fun invoke(recipeId: String, onError: suspend (Throwable) -> Unit = {}): Flow<Recipe?>
}

internal class ObserveRecipeUseCase(
    private val recipeRepo: IRecipeRepo,
    private val latestRecipesRepo: ILatestRecipesRepo,
) : IObserveRecipeUseCase {

    override suspend fun invoke(recipeId: String, onError: suspend (Throwable) -> Unit) = flow {
        try {
            latestRecipesRepo.pushRecipe(recipeId)
            recipeRepo.observeRecipe(recipeId).collect { recipe -> emit(recipe) }
        } catch (e: Throwable) {
            onError(e)
        }
    }

}
