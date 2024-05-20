package io.chefbook.sdk.recipe.community.api.external.domain.usecases

import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesFilter
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

interface GetRecipesUseCase {

  suspend operator fun invoke(filter: RecipesFilter): Result<List<DecryptedRecipeInfo>>
}
