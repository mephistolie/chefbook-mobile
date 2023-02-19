package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo

interface ISetRecipeSaveStatusUseCase {
  suspend operator fun invoke(recipeId: String, saved: Boolean): SimpleAction
}

internal class SetRecipeSaveStatusUseCase(
  private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeSaveStatusUseCase {

  override suspend operator fun invoke(recipeId: String, saved: Boolean) =
    recipeRepo.setRecipeSavedStatus(recipeId, saved)

}
