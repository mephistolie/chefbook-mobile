package io.chefbook.sdk.recipe.interaction.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

fun interface SetRecipeCategoriesUseCase {
  suspend operator fun invoke(recipeId: String, categories: List<String>): EmptyResult
}
