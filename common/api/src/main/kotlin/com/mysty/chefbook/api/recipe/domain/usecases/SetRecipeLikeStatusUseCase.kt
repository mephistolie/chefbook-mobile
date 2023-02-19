package com.mysty.chefbook.api.recipe.domain.usecases

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo

interface ISetRecipeLikeStatusUseCase {
  suspend operator fun invoke(recipeId: String, liked: Boolean): SimpleAction
}

internal class SetRecipeLikeStatusUseCase(
  private val recipeRepo: IRecipeInteractionRepo,
) : ISetRecipeLikeStatusUseCase {

  override suspend operator fun invoke(recipeId: String, liked: Boolean): SimpleAction =
    recipeRepo.setRecipeLikeStatus(recipeId, liked)

}
