package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

interface IObserveRecipeBookUseCase {
    suspend operator fun invoke(): StateFlow<List<RecipeInfo>?>
}

class ObserveRecipeBookUseCase @Inject constructor(
    private val recipeRepo: IRecipeRepo,
) : IObserveRecipeBookUseCase {

    override suspend operator fun invoke() = recipeRepo.observeRecipeBook()

}
