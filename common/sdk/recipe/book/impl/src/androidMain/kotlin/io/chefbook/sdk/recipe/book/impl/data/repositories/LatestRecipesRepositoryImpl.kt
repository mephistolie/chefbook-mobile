package io.chefbook.sdk.recipe.book.impl.data.repositories

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.sdk.recipe.book.api.external.domain.entities.LatestRecipeInfo
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore.LatestRecipesSerializer
import io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore.dto.LatestRecipeInfoSerializable
import io.chefbook.sdk.recipe.book.impl.data.sources.local.datastore.dto.toSerializable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class LatestRecipesRepositoryImpl(
  context: Context,
) : LatestRecipesRepository {

  private val dataStore = DataStoreFactory.create(
    serializer = LatestRecipesSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

  override fun observeLatestRecipes() =
    dataStore.data.map { recipes -> recipes.map(LatestRecipeInfoSerializable::toEntity) }

  override suspend fun getLatestRecipes() =
    observeLatestRecipes().first()

  override suspend fun pushRecipe(recipe: LatestRecipeInfo) {
    dataStore.updateData { savedLatestRecipes ->
      val filteredLatestRecipes = savedLatestRecipes.filter { it.id != recipe.id }
      var latestRecipes = listOf(recipe.toSerializable()) + filteredLatestRecipes
      if (latestRecipes.size > LATEST_RECIPES_CACHE_SIZE) {
        latestRecipes = latestRecipes.subList(0, LATEST_RECIPES_CACHE_SIZE)
      }
      latestRecipes
    }
  }

  override suspend fun deleteRecipe(recipeId: String) {
    dataStore.updateData { recipes -> recipes.filter { it.id != recipeId } }
  }

  override suspend fun clear() {
    dataStore.updateData { LatestRecipesSerializer.defaultValue }
  }

  companion object {
    private const val LATEST_RECIPES_CACHE_SIZE = 20
    private const val DATASTORE_FILE = "latest_recipes.json"
  }
}
