package io.chefbook.sdk.recipe.book.impl.data.repositories

import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import kotlinx.coroutines.flow.Flow

internal class LatestRecipesRepositoryImpl() : LatestRecipesRepository {

  override fun observeLatestRecipes(): Flow<List<String>> = TODO()

  override suspend fun getLatestRecipes(): List<String> = TODO()

  override suspend fun pushRecipe(recipeId: String) {

  }

  override suspend fun clear() {
    TODO("Not yet implemented")
  }
}
