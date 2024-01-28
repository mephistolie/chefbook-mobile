package io.chefbook.sdk.recipe.crud.impl.data.sources.remote

import io.chefbook.sdk.recipe.crud.api.internal.data.models.RecipeProcessedInput
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.RecipeCrudSource

internal interface RemoteRecipeCrudSource : RecipeCrudSource {

  suspend fun createRecipe(input: RecipeProcessedInput): Result<String>

  suspend fun updateRecipe(input: RecipeProcessedInput): Result<Int>
}
