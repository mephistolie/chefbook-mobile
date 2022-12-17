package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import kotlinx.coroutines.flow.StateFlow

interface IObserveRecipeBookUseCase {
    suspend operator fun invoke(): StateFlow<List<RecipeInfo>?>
}

internal class ObserveRecipeBookUseCase(
    private val recipeRepo: IRecipeRepo,
) : IObserveRecipeBookUseCase {

    override suspend operator fun invoke() = recipeRepo.observeRecipeBook()

}
