package io.chefbook.sdk.recipe.crud.api.internal.data.sources.local

import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.RecipeCrudSource
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

interface LocalRecipeCrudSource : RecipeCrudSource {
  
  suspend fun createRecipe(recipe: Recipe): Result<String>
  
  suspend fun updateRecipe(recipe: Recipe): EmptyResult

  suspend fun setRecipeOwnerInfo(
    recipeId: String,
    name: String?,
    avatar: String?,
    ): EmptyResult

  suspend fun setRecipeTags(recipeId: String, tags: List<Tag>): EmptyResult
}
