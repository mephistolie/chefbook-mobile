package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo
import javax.inject.Inject

interface IGetLatestRecipesUseCase {
    suspend operator fun invoke(): List<String>
}

class GetLatestRecipesUseCase @Inject constructor(
    private val recipeRepo: ILatestRecipesRepo,
) : IGetLatestRecipesUseCase {

    override suspend operator fun invoke() = recipeRepo.getLatestRecipes()

}
