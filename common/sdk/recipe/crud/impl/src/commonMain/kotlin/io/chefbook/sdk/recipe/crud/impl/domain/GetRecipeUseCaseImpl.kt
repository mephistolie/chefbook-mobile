package io.chefbook.sdk.recipe.crud.impl.domain

import io.chefbook.sdk.recipe.book.api.external.domain.entities.asLatestRecipeInfo
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.GetRecipeUseCase
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepository

internal class GetRecipeUseCaseImpl(
  private val recipeRepository: RecipeCrudRepository,
  private val latestRecipesRepository: LatestRecipesRepository,
) : GetRecipeUseCase {

  override suspend operator fun invoke(recipeId: String) =
    recipeRepository.getRecipe(recipeId)
      .onSuccess { recipe ->
        if (recipe is DecryptedRecipe) {
          latestRecipesRepository.pushRecipe(recipe.asLatestRecipeInfo())
        } else {
          latestRecipesRepository.deleteRecipe(recipeId)
        }
      }
      .onFailure {
        latestRecipesRepository.deleteRecipe(recipeId)
      }
}
