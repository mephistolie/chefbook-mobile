package io.chefbook.sdk.recipe.book.api.external.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.entities.LatestRecipeInfo

interface GetLatestRecipesUseCase {

  suspend operator fun invoke(): List<LatestRecipeInfo>
}