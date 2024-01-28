package io.chefbook.sdk.recipe.interaction.impl.data.sources.remote

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.interaction.api.internal.data.sources.RecipeInteractionSource

internal interface RemoteRecipeInteractionSource : RecipeInteractionSource {

  suspend fun saveRecipe(recipeId: String): EmptyResult

  suspend fun removeRecipeFromRecipeBook(recipeId: String): EmptyResult
}
