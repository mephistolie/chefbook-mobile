package io.chefbook.sdk.recipe.community.api.external.domain.entities

import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo

data class RecipesFilter(
  val lastRecipe: RecipeInfo? = null,
  val recipesCount: Int? = DEFAULT_RECIPES_COUNT,
  val sorting: RecipesSorting? = null,

  val tags: List<String>? = null,

  val search: String? = null,

  val minRating: Int? = null,

  val maxTime: Int? = null,
  val minServings: Int? = null,
  val minCalories: Int? = null,
  val maxCalories: Int? = null,
) {

  companion object {
    const val DEFAULT_RECIPES_COUNT = 20
  }
}
