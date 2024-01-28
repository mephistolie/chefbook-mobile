package io.chefbook.sdk.recipe.crud.api.internal.data.sources

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe

interface RecipeCrudSource {

  suspend fun getRecipe(recipeId: String): Result<Recipe>

  suspend fun deleteRecipe(recipeId: String): EmptyResult
}
