package io.chefbook.sdk.recipe.community.impl.data.models

import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesSorting

internal data class RecipesQuery(
  val recipesCount: Int? = null,

  val recipeIds: List<String>? = null,
  val authorId: String? = null,

  val owned: Boolean? = null,
  val saved: Boolean? = null,

  val tags: List<String>? = null,

  val search: String? = null,

  val minRating: Int? = null,
  val maxRating: Int? = null,

  val minTime: Int? = null,
  val maxTime: Int? = null,
  val minServings: Int? = null,
  val maxServings: Int? = null,
  val minCalories: Int? = null,
  val maxCalories: Int? = null,

  val sorting: RecipesSorting? = null,
  val lastRecipeId: String? = null,
  val lastCreationTimestamp: String? = null,
  val lastUpdateTimestamp: String? = null,
  val lastRating: Float? = null,
  val lastVotes: Int? = null,
  val lastTime: Int? = null,
  val lastCalories: Int? = null,

  val recipeLanguages: List<String>? = null,
  val userLanguage: String? = null,
)
