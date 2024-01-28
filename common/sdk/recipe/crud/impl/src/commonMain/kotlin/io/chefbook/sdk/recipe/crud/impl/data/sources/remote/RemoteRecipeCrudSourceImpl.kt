package io.chefbook.sdk.recipe.crud.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.withCast
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.GetRecipeResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.toEntity
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.toSerializable
import io.chefbook.sdk.recipe.crud.api.internal.data.models.RecipeProcessedInput
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.RecipeCrudApiService
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.CreateRecipeResponse

internal class RemoteRecipeCrudSourceImpl(
  private val api: RecipeCrudApiService,
) : RemoteRecipeCrudSource {

//  override suspend fun getRecipesByQuery(query: RecipesFilter) =
//    api.getRecipes(
//      GetRecipesRequest(
//        search = query.search,
//        owned = query.saved,
//        saved = query.saved,
//        pageSize = query.pageSize,
//        minTime = query.minTime,
//        maxTime = query.maxTime,
//        minServings = query.minServings,
//        maxServings = query.maxServings,
//        minCalories = query.minCalories,
//        maxCalories = query.maxCalories,
//        sortBy = query.sortBy.toString(),
//      )
//    ).withCast(GetRecipesResponse::toEntity)

  override suspend fun createRecipe(input: RecipeProcessedInput) =
    api.createRecipe(input.toSerializable())
      .withCast(CreateRecipeResponse::recipeId)

  override suspend fun getRecipe(recipeId: String) =
    api.getRecipe(recipeId).withCast(GetRecipeResponse::toEntity)

  override suspend fun updateRecipe(input: RecipeProcessedInput): Result<Int> {
    return api.updateRecipe(input.id, input.toSerializable()).withCast(VersionResponse::version)
  }

  override suspend fun deleteRecipe(recipeId: String) =
    api.deleteRecipe(recipeId).asEmpty()
}
