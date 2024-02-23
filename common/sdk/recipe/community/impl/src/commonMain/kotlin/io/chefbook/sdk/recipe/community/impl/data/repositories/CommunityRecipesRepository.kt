package io.chefbook.sdk.recipe.community.impl.data.repositories

import io.chefbook.sdk.recipe.community.impl.data.models.RecipesQuery
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal interface CommunityRecipesRepository {

  suspend fun getRecipes(query: RecipesQuery): Result<List<DecryptedRecipeInfo>>
}
