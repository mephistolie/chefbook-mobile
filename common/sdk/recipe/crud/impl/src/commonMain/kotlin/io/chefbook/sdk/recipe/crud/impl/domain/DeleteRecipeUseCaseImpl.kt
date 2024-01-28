package io.chefbook.sdk.recipe.crud.impl.domain

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.DeleteRecipeUseCase
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepository

internal class DeleteRecipeUseCaseImpl(
  private val recipeRepository: RecipeCrudRepository,
  private val latestRecipesRepository: LatestRecipesRepository,
) : DeleteRecipeUseCase {

  override suspend operator fun invoke(recipeId: String): EmptyResult {
    latestRecipesRepository.deleteRecipe(recipeId)
    return recipeRepository.deleteRecipe(recipeId)
  }
}
