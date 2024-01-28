package io.chefbook.sdk.recipe.interaction.impl.data.repositories

import io.chefbook.libs.exceptions.LocalProfileException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.recipe.core.api.internal.data.cache.RecipesCache
import io.chefbook.sdk.recipe.interaction.api.internal.data.sources.local.LocalRecipeInteractionSource
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.RemoteRecipeInteractionSource

internal class RecipeInteractionRepositoryImpl(
  private val localSource: LocalRecipeInteractionSource,
  private val remoteSource: RemoteRecipeInteractionSource,

  private val cache: RecipesCache,
  private val sourceRepository: DataSourcesRepository,
) : RecipeInteractionRepository {

  override suspend fun setRecipeScore(recipeId: String, score: Int?): EmptyResult {
    if (!sourceRepository.isRemoteSourceEnabled()) return Result.failure(LocalProfileException)
    return remoteSource.setRecipeScore(recipeId, score)
      .onSuccess {
        cache.setRecipeScore(recipeId, score)
        val rating = cache.getRecipe(recipeId)?.rating
        if (rating != null) {
          localSource.setRecipeRating(recipeId, rating)
        } else {
          localSource.setRecipeScore(recipeId, score)
        }
      }
  }

  override suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean): EmptyResult {
    if (!sourceRepository.isRemoteSourceEnabled()) return Result.failure(LocalProfileException)
    val result =
      if (saved) {
        remoteSource.saveRecipe(recipeId)
      } else {
        remoteSource.removeRecipeFromRecipeBook(recipeId)
      }

    if (result.isSuccess) cache.setRecipeSavedStatus(recipeId, saved)

    return result
  }

  override suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean): EmptyResult {
    val result = if (sourceRepository.isRemoteSourceEnabled()) {
      remoteSource.setRecipeFavouriteStatus(recipeId, favourite)
    } else {
      localSource.setRecipeFavouriteStatus(recipeId, favourite)
    }

    if (result.isSuccess) {
      cache.setRecipeFavouriteStatus(recipeId, favourite)
      if (sourceRepository.isRemoteSourceEnabled()) localSource.setRecipeFavouriteStatus(
        recipeId,
        favourite
      )
    }

    return result
  }

  override suspend fun setRecipeCategories(
    recipeId: String,
    categories: List<String>
  ): EmptyResult {
    val result = if (sourceRepository.isRemoteSourceEnabled()) {
      remoteSource.setRecipeCategories(recipeId, categories)
    } else {
      localSource.setRecipeCategories(recipeId, categories)
    }

    if (result.isSuccess) {
      cache.setRecipeCategories(recipeId, categories)
      if (sourceRepository.isRemoteSourceEnabled()) localSource.setRecipeCategories(
        recipeId,
        categories
      )
    }

    return result
  }
}
