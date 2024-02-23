package io.chefbook.sdk.recipe.book.impl.data.sources.remote.services

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto.GetRecipeBookResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.url

internal class RecipeBookApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), RecipeBookApiService {

  override suspend fun getRecipeBook(): Result<GetRecipeBookResponse> = safeGet(RECIPE_BOOK_ROUTE)

  companion object {
    private const val RECIPE_BOOK_ROUTE = "/v1/recipes/book"
  }
}
