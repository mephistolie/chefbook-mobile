package io.chefbook.sdk.recipe.crud.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.recipe.crud.api.internal.data.models.RecipeProcessedInput
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.RecipeCrudApiService
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.CreateRecipeResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.GetRecipeResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.toEntity
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.toSerializable

internal class RemoteRecipeCrudSourceImpl(
  private val api: RecipeCrudApiService,
) : RemoteRecipeCrudSource {

  override suspend fun createRecipe(input: RecipeProcessedInput) =
    api.createRecipe(input.toSerializable())
      .map(CreateRecipeResponse::recipeId)

  override suspend fun getRecipe(recipeId: String) =
    api.getRecipe(recipeId).map(GetRecipeResponse::toEntity)

  override suspend fun updateRecipe(input: RecipeProcessedInput): Result<Int> {
    return api.updateRecipe(input.id, input.toSerializable()).map(VersionResponse::version)
  }

  override suspend fun deleteRecipe(recipeId: String) =
    api.deleteRecipe(recipeId).asEmpty()
}
