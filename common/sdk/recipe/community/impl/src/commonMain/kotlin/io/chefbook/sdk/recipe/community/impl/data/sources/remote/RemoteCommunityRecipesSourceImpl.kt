package io.chefbook.sdk.recipe.community.impl.data.sources.remote

import io.chefbook.sdk.recipe.community.impl.data.models.RecipesQuery
import io.chefbook.sdk.recipe.community.impl.data.sources.CommunityRecipesSource
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.CommunityRecipesApiService
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.dto.GetRecipesResponse
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.dto.toEntity
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal class RemoteCommunityRecipesSourceImpl(
  private val api: CommunityRecipesApiService,
) : CommunityRecipesSource {

  override suspend fun getRecipes(query: RecipesQuery): Result<List<DecryptedRecipeInfo>> =
    api.getRecipes(query).map(GetRecipesResponse::toEntity)
}

