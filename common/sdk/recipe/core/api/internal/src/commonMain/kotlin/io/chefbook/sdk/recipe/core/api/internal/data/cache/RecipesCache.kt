package io.chefbook.sdk.recipe.core.api.internal.data.cache

import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import kotlinx.coroutines.flow.Flow

interface RecipesCacheReader {

  fun observeRecipes(): Flow<Map<String, Recipe>>

  fun observeRecipe(recipeId: String): Flow<Recipe?>

  suspend fun getRecipe(recipeId: String): Recipe?
}

interface RecipesCacheWriter {
  suspend fun setRecipeBook(recipes: List<RecipeInfo>)
  suspend fun putRecipe(recipe: Recipe)
  suspend fun removeRecipe(recipeId: String)

  suspend fun setRecipeScore(recipeId: String, score: Int?)
  suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean)
  suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean)
  suspend fun setRecipeCategories(recipeId: String, categories: List<String>)

  suspend fun clear()
}

interface RecipesCache : RecipesCacheReader, RecipesCacheWriter
