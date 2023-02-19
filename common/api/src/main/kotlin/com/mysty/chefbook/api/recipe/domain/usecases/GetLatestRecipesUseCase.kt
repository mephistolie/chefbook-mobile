package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.recipe.domain.ILatestRecipesRepo

interface IGetLatestRecipesUseCase {
    suspend operator fun invoke(): List<String>
}

internal class GetLatestRecipesUseCase(
    private val recipeRepo: ILatestRecipesRepo,
) : IGetLatestRecipesUseCase {

    override suspend operator fun invoke() = recipeRepo.getLatestRecipes()

}
