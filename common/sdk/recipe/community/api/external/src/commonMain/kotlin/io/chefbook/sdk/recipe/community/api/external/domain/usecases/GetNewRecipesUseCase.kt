package io.chefbook.sdk.recipe.community.api.external.domain.usecases

import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo

interface GetNewRecipesUseCase {

  suspend operator fun invoke(lastRecipe: RecipeInfo? = null): Result<List<DecryptedRecipeInfo>>
}
