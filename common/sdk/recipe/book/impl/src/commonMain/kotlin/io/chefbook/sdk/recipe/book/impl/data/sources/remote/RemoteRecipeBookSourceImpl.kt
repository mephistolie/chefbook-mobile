package io.chefbook.sdk.recipe.book.impl.data.sources.remote

import io.chefbook.libs.utils.result.withCast
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.RecipeBookApiService
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto.GetRecipeBookResponse
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto.toModel

internal class RemoteRecipeBookSourceImpl(
  private val api: RecipeBookApiService,
) : RemoteRecipeBookSource {

  override suspend fun getRecipeBook() =
    api.getRecipeBook().withCast(GetRecipeBookResponse::toModel)
}
