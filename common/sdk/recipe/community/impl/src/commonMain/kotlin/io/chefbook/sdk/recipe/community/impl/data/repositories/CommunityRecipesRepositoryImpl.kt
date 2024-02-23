package io.chefbook.sdk.recipe.community.impl.data.repositories

import io.chefbook.sdk.recipe.community.impl.data.models.RecipesQuery
import io.chefbook.sdk.recipe.community.impl.data.sources.CommunityRecipesSource

internal class CommunityRecipesRepositoryImpl(
  private val source: CommunityRecipesSource,
) : CommunityRecipesRepository {

  override suspend fun getRecipes(query: RecipesQuery) = source.getRecipes(query)
}
