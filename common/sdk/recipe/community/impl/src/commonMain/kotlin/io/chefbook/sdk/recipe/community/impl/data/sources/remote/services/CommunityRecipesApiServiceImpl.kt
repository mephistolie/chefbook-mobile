package io.chefbook.sdk.recipe.community.impl.data.sources.remote.services

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesSorting
import io.chefbook.sdk.recipe.community.impl.data.models.RecipesQuery
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.dto.GetRecipesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.parameters

internal class CommunityRecipesApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), CommunityRecipesApiService {
  override suspend fun getRecipes(query: RecipesQuery): Result<GetRecipesResponse> =
    safeGet(RECIPES_ROUTE) {
      parameter("count", query.recipesCount)


      query.recipeIds?.forEach { parameter("recipe_id", it) }
      parameter("author_id", query.authorId)

      parameter("owned", query.owned)
      parameter("saved", query.saved)

      query.tags?.forEach { parameter("tag", it) }

      parameter("search", query.search)

      parameter("sorting", when (query.sorting) {
        RecipesSorting.CREATION_TIMESTAMP -> "creation_timestamp"
        RecipesSorting.UPDATE_TIMESTAMP -> "update_timestamp"
        RecipesSorting.RATING -> "rating"
        RecipesSorting.VOTES -> "votes"
        RecipesSorting.TIME -> "cooking_time"
        RecipesSorting.CALORIES -> "calories"
        null -> null
      })
      parameter("last_recipe_id", query.lastRecipeId)
      parameter("last_creation_timestamp", query.lastCreationTimestamp)
      parameter("last_update_timestamp", query.lastUpdateTimestamp)
      parameter("last_rating", query.lastRating)
      parameter("last_votes", query.lastVotes)
      parameter("last_time", query.lastTime)
      parameter("last_calories", query.lastCalories)

      parameter("min_rating", query.minRating)
      parameter("max_rating", query.maxRating)

      parameter("min_time", query.minTime)
      parameter("max_time", query.maxTime)
      parameter("min_servings", query.minServings)
      parameter("max_servings", query.maxServings)
      parameter("min_calories", query.minCalories)
      parameter("max_calories", query.maxCalories)

      query.recipeLanguages?.forEach { parameter("recipe_language", it) }
      parameter("user_language", query.userLanguage)
    }

  companion object {
    private const val RECIPES_ROUTE = "/v1/recipes"
  }
}
