package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo

interface ISetRecipeFavouriteStatusUseCase {
  suspend operator fun invoke(recipeId: String, favourite: Boolean): SimpleAction
}

internal class SetRecipeFavouriteStatusUseCase(
  private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeFavouriteStatusUseCase {

  override suspend operator fun invoke(recipeId: String, favourite: Boolean): SimpleAction =
    recipeRepo.setRecipeFavouriteStatus(recipeId, favourite)

}
