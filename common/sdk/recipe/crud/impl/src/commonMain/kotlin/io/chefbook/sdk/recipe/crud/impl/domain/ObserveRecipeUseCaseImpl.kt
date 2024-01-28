package io.chefbook.sdk.recipe.crud.impl.domain

import io.chefbook.sdk.recipe.book.api.external.domain.entities.asLatestRecipeInfo
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipeUseCase
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepository
import kotlinx.coroutines.flow.onEach

internal class ObserveRecipeUseCaseImpl(
  private val recipeRepository: RecipeCrudRepository,
  private val latestRecipesRepository: LatestRecipesRepository,
) : ObserveRecipeUseCase {

  override suspend fun invoke(recipeId: String) =
    recipeRepository.observeRecipe(recipeId)
      .onEach { recipe ->
        if (recipe is DecryptedRecipe && !recipe.isEncryptionEnabled) {
          latestRecipesRepository.pushRecipe(recipe.asLatestRecipeInfo())
        } else {
          latestRecipesRepository.deleteRecipe(recipeId)
        }
      }
}
