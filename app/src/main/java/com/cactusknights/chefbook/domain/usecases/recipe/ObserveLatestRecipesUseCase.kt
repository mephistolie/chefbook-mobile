package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo
import kotlinx.coroutines.flow.Flow

interface IObserveLatestRecipesUseCase {
    suspend operator fun invoke(): Flow<List<String>>
}

class ObserveLatestRecipesUseCase(
    private val recipeRepo: ILatestRecipesRepo,
) : IObserveLatestRecipesUseCase {

    override suspend operator fun invoke() = recipeRepo.observeLatestRecipes()

}
