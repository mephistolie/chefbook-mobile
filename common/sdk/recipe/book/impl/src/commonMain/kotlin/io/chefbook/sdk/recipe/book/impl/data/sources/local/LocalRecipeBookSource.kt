package io.chefbook.sdk.recipe.book.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo

internal interface LocalRecipeBookSource {

  suspend fun getRecipeBook(): Result<List<RecipeInfo>>

  suspend fun clearData(exceptProfileId: String? = null): EmptyResult
}
