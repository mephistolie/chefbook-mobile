package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

interface IObserveRecipeBookUseCase {
    operator fun invoke(): Flow<List<RecipeInfo>?>
}

internal class ObserveRecipeBookUseCase(
    private val recipeRepo: IRecipeRepo,
) : IObserveRecipeBookUseCase {

    override operator fun invoke() = recipeRepo.observeRecipeBook()
        .onStart { emit(null) }

}
