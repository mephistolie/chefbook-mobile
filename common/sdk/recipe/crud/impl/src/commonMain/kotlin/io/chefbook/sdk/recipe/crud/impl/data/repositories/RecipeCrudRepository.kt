package io.chefbook.sdk.recipe.crud.impl.data.repositories

import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import kotlinx.coroutines.flow.Flow

internal interface RecipeCrudRepository {

  fun observeRecipes(): Flow<Map<String, Recipe>>

  suspend fun observeRecipe(recipeId: String): Flow<Recipe?>

  suspend fun getRecipe(recipeId: String): Result<Recipe>

  suspend fun createRecipe(input: RecipeInput, key: SymmetricKey?): Result<String>

  suspend fun updateRecipe(
    input: RecipeInput,
    key: SymmetricKey?
  ): Result<Recipe>

  suspend fun deleteRecipe(recipeId: String): EmptyResult
}
