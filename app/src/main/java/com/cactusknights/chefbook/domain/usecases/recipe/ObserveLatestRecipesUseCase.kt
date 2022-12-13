package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface IObserveLatestRecipesUseCase {
    suspend operator fun invoke(): Flow<List<String>>
}

class ObserveLatestRecipesUseCase @Inject constructor(
    private val recipeRepo: ILatestRecipesRepo,
) : IObserveLatestRecipesUseCase {

    override suspend operator fun invoke() = recipeRepo.observeLatestRecipes()

}
