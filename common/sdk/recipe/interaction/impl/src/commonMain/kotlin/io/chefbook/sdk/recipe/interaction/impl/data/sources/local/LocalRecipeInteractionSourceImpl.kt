package io.chefbook.sdk.recipe.interaction.impl.data.sources.local

import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.database.api.internal.toLong
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.interaction.api.internal.data.sources.local.LocalRecipeInteractionSource

internal class LocalRecipeInteractionSourceImpl(
  private val profileId: String,
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalRecipeInteractionSource {

  private val recipeQueries = database.recipeQueries
  private val recipeCollectionQueries = database.recipeCollectionQueries

  override suspend fun setRecipeRating(recipeId: String, rating: RecipeMeta.Rating) = safeQueryResult {
    recipeQueries.transaction {
      recipeQueries.setRating(
        rating.index.toDouble(),
        rating.votes.toLong(),
        recipeId,
      )
      recipeQueries.setScore(rating.score?.toLong(), recipeId, profileId)
    }
  }

  override suspend fun setRecipeScore(recipeId: String, score: Int?) = safeQueryResult {
    recipeQueries.setScore(score?.toLong(), recipeId, profileId)
  }

  override suspend fun setRecipeFavouriteStatus(
    recipeId: String,
    isFavourite: Boolean
  ) = safeQueryResult {
    recipeQueries.setFavourite(isFavourite.toLong(), recipeId, profileId)
  }

  override suspend fun setRecipeCollections(
    recipeId: String,
    collections: List<String>
  ) = safeQueryResult {
    recipeCollectionQueries.transaction {
      recipeCollectionQueries.clearRecipeCollections(recipeId, profileId)
      collections.forEach { collectionId -> recipeCollectionQueries.insert(recipeId, collectionId) }
    }
  }
}
