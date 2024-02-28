package io.chefbook.sdk.recipe.book.impl.data.sources.remote

import io.chefbook.libs.utils.language.getSystemLanguageCode
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.RecipeBookApiService
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto.GetRecipeBookResponse
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto.toModel

internal class RemoteRecipeBookSourceImpl(
  private val api: RecipeBookApiService,
) : RemoteRecipeBookSource {

  override suspend fun getRecipeBook() =
    api.getRecipeBook(userLanguage = getSystemLanguageCode()).map(GetRecipeBookResponse::toModel)
}
