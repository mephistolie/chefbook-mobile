package io.chefbook.sdk.recipe.interaction.impl.data.sources.local

import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.database.api.internal.RecipeCategory
import io.chefbook.sdk.database.api.internal.toLong
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.interaction.api.internal.data.sources.local.LocalRecipeInteractionSource

internal class LocalRecipeInteractionSourceImpl(
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalRecipeInteractionSource {

  private val recipeQueries = database.recipeQueries
  private val recipeCategoryQueries = database.recipeCategoryQueries

  override suspend fun setRecipeRating(recipeId: String, rating: RecipeMeta.Rating) = safeQueryResult {
    recipeQueries.setRating(
      rating.index.toDouble(),
      rating.score?.toLong(),
      rating.votes.toLong(),
      recipeId,
    )
  }

  override suspend fun setRecipeScore(recipeId: String, score: Int?) = safeQueryResult {
    recipeQueries.setScore(score?.toLong(), recipeId)
  }

  override suspend fun setRecipeFavouriteStatus(
    recipeId: String,
    isFavourite: Boolean
  ) = safeQueryResult {
    recipeQueries.setFavourite(isFavourite.toLong(), recipeId)
  }

  override suspend fun setRecipeCategories(
    recipeId: String,
    categories: List<String>
  ) = safeQueryResult {
    recipeCategoryQueries.transaction {
      recipeCategoryQueries.clearRecipeCategories(recipeId)
      categories.forEach { categoryId -> recipeCategoryQueries.insert(RecipeCategory(recipeId, categoryId)) }
    }
  }
}
