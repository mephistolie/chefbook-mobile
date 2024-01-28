package io.chefbook.sdk.recipe.book.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.book.api.external.domain.entities.RecipeBook
import kotlinx.coroutines.flow.Flow

interface RecipeBookRepository {

  fun observeRecipeBook(): Flow<RecipeBook?>

  suspend fun getRecipeBook(): RecipeBook

  suspend fun refreshRecipeBook(): EmptyResult

  suspend fun clearLocalData(exceptProfileId: String? = null): EmptyResult
}
