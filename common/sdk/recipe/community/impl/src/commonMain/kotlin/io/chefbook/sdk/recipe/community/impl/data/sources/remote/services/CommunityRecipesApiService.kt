package io.chefbook.sdk.recipe.community.impl.data.sources.remote.services

import io.chefbook.sdk.recipe.community.impl.data.models.RecipesQuery
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.dto.GetRecipesResponse

internal interface CommunityRecipesApiService {

  suspend fun getRecipes(query: RecipesQuery): Result<GetRecipesResponse>
}
