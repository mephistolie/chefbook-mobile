package io.chefbook.sdk.recipe.book.impl.data.sources.remote

import io.chefbook.sdk.recipe.book.api.internal.data.models.RecipeBookState

internal interface RemoteRecipeBookSource {

  suspend fun getRecipeBook(): Result<RecipeBookState>
}
