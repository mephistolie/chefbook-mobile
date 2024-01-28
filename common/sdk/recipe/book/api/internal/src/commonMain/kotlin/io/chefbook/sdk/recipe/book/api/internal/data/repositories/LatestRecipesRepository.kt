package io.chefbook.sdk.recipe.book.api.internal.data.repositories

import io.chefbook.sdk.recipe.book.api.external.domain.entities.LatestRecipeInfo
import kotlinx.coroutines.flow.Flow

interface LatestRecipesRepository {

  fun observeLatestRecipes(): Flow<List<LatestRecipeInfo>>

  suspend fun getLatestRecipes(): List<LatestRecipeInfo>

  suspend fun pushRecipe(recipe: LatestRecipeInfo)

  suspend fun deleteRecipe(recipeId: String)

  suspend fun clear()
}
