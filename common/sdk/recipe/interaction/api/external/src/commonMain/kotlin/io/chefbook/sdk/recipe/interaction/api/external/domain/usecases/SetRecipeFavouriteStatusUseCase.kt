package io.chefbook.sdk.recipe.interaction.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SetRecipeFavouriteStatusUseCase {
  suspend operator fun invoke(recipeId: String, favourite: Boolean): EmptyResult
}

