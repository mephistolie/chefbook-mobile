package io.chefbook.sdk.recipe.community.impl.domain

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.utils.language.getSystemLanguageCode
import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesFilter
import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesSorting
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetRecipesUseCase
import io.chefbook.sdk.recipe.community.impl.data.models.RecipesQuery
import io.chefbook.sdk.recipe.community.impl.data.repositories.CommunityRecipesRepository
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class GetRecipesUseCaseImpl(
  private val recipesRepository: CommunityRecipesRepository,
  private val settingsRepository: SettingsRepository,
) : GetRecipesUseCase {

  override suspend fun invoke(filter: RecipesFilter) = recipesRepository.getRecipes(
    RecipesQuery(
      recipesCount = filter.recipesCount,

      search = filter.search,

      tags = filter.tags,

      minRating = filter.minRating,

      maxTime = filter.maxTime,
      minServings = filter.minServings,
      minCalories = filter.minCalories,
      maxCalories = filter.maxCalories,

      sorting = filter.sorting,

      lastRecipeId = filter.lastRecipe?.id,
      lastCreationTimestamp = filter.lastRecipe?.creationTimestamp,
      lastUpdateTimestamp = filter.lastRecipe?.updateTimestamp,
      lastRating = filter.lastRecipe?.rating?.index,
      lastVotes = filter.lastRecipe?.rating?.votes,
      lastTime = filter.lastRecipe?.time,
      lastCalories = filter.lastRecipe?.calories,

      recipeLanguages = settingsRepository.getCommunityRecipesLanguages().map(Language::code),
      userLanguage = getSystemLanguageCode(),
    )
  )
}
