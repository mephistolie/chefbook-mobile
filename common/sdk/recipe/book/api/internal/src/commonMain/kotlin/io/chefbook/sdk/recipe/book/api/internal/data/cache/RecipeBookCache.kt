package io.chefbook.sdk.recipe.book.api.internal.data.cache

import io.chefbook.sdk.recipe.book.api.external.domain.entities.RecipeBook
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCache
import kotlinx.coroutines.flow.Flow

interface RecipeBookCache : RecipesCache {
  fun observeRecipeBook(): Flow<RecipeBook?>
  suspend fun getRecipeBook(): RecipeBook
}
