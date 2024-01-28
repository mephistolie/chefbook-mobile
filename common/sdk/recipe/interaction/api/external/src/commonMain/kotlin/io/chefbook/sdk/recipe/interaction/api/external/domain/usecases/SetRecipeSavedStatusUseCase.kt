package io.chefbook.sdk.recipe.interaction.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SetRecipeSavedStatusUseCase {
  suspend operator fun invoke(recipeId: String, saved: Boolean): EmptyResult
}
