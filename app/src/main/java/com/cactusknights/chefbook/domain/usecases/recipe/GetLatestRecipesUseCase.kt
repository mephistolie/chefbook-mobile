package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo

interface IGetLatestRecipesUseCase {
    suspend operator fun invoke(): List<String>
}

class GetLatestRecipesUseCase(
    private val recipeRepo: ILatestRecipesRepo,
) : IGetLatestRecipesUseCase {

    override suspend operator fun invoke() = recipeRepo.getLatestRecipes()

}
