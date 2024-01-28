package io.chefbook.sdk.recipe.book.impl.data.sources.remote.services

import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto.GetRecipeBookResponse

internal interface RecipeBookApiService {

  suspend fun getRecipeBook(): Result<GetRecipeBookResponse>
}
