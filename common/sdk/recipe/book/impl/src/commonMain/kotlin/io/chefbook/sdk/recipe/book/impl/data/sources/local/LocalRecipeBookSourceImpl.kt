package io.chefbook.sdk.recipe.book.impl.data.sources.local

import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.core.api.internal.data.sources.local.sql.dto.toEntity

internal class LocalRecipeBookSourceImpl(
  private val profileId: String,
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalRecipeBookSource {

  private val recipeQueries = database.recipeQueries
  private val recipeCollectionQueries = database.recipeCollectionQueries

  override suspend fun getRecipeBook(): Result<List<RecipeInfo>> = safeQueryResult {
    val recipeCategories = recipeCollectionQueries.().executeAsList()

    recipeQueries.selectAll(profileId).executeAsList().asSequence()
      .map { it.toEntity(recipeCategories) }
      .map(Recipe::info)
      .toList()
  }

  override suspend fun clearData(exceptProfileId: String?) = safeQueryResult {
    if (exceptProfileId != null) {
      recipeQueries.clearExceptUser(exceptProfileId)
    } else {
      recipeQueries.clear()
    }
  }
}
