package io.chefbook.sdk.recipe.interaction.api.internal.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.interaction.api.internal.data.sources.RecipeInteractionSource

interface LocalRecipeInteractionSource : RecipeInteractionSource {
  suspend fun setRecipeRating(
    recipeId: String,
    rating: RecipeMeta.Rating,
  ): EmptyResult
}
