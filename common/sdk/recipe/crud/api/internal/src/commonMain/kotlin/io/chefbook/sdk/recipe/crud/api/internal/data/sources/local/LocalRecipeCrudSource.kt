package io.chefbook.sdk.recipe.crud.api.internal.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.RecipeCrudSource

interface LocalRecipeCrudSource : RecipeCrudSource {
  
  suspend fun createRecipe(recipe: Recipe): Result<String>
  
  suspend fun updateRecipe(recipe: Recipe): EmptyResult
}
