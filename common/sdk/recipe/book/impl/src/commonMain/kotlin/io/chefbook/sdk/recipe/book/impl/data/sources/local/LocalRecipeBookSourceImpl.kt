package io.chefbook.sdk.recipe.book.impl.data.sources.local

import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.core.api.internal.data.sources.local.sql.dto.toEntity

internal class LocalRecipeBookSourceImpl(
  private val profileId: String,
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalRecipeBookSource {

  private val queries = database.recipeQueries

  override suspend fun getRecipeBook(): Result<List<RecipeInfo>> = safeQueryResult {
    queries.transactionWithResult {
      queries.selectAll(profileId).executeAsList().asSequence()
        .map { recipe ->
          recipe.toEntity(
            collections = queries.getCollections(
              recipeId = recipe.recipeId,
              profileId = profileId,
            ).executeAsList(),
          )
        }
        .toList()
    }
  }

  override suspend fun clearUnused() = safeQueryResult {
    queries.clearUnused()
  }
}
