package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.recipe.domain.ILatestRecipesRepo
import kotlinx.coroutines.flow.Flow

interface IObserveLatestRecipesUseCase {
    operator fun invoke(): Flow<List<String>>
}

internal class ObserveLatestRecipesUseCase(
    private val recipeRepo: ILatestRecipesRepo,
) : IObserveLatestRecipesUseCase {

    override operator fun invoke() = recipeRepo.observeLatestRecipes()

}
