package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import kotlinx.coroutines.flow.StateFlow

interface IObserveRecipeBookUseCase {
    suspend operator fun invoke(): StateFlow<List<RecipeInfo>?>
}

class ObserveRecipeBookUseCase(
    private val recipeRepo: IRecipeRepo,
) : IObserveRecipeBookUseCase {

    override suspend operator fun invoke() = recipeRepo.observeRecipeBook()

}
