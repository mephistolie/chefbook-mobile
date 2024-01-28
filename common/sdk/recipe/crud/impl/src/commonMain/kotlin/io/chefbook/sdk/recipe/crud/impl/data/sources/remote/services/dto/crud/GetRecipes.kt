package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeCategoryInfoBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeTagBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRecipesRequest(
  @SerialName("pageSize")
  val pageSize: Int? = null,

  @SerialName("authorId")
  val authorId: String? = null,

  @SerialName("owned")
  val owned: Boolean? = null,
  @SerialName("saved")
  val saved: Boolean? = null,

  @SerialName("search")
  val search: String? = null,

  @SerialName("sorting")
  val sortBy: String? = null,
  @SerialName("lastRecipeId")
  val lastRecipeId: String? = null,
  @SerialName("lastCreationTimestamp")
  val lastCreationTimestamp: String? = null,
  @SerialName("lastUpdateTimestamp")
  val lastUpdateTimestamp: String? = null,
  @SerialName("lastRating")
  val lastRating: Float? = null,
  @SerialName("lastVotes")
  val lastVotes: Int? = null,
  @SerialName("lastTime")
  val lastTime: String? = null,
  @SerialName("lastCalories")
  val lastCalories: Int? = null,

  @SerialName("minRating")
  val minRating: Int? = null,
  @SerialName("maxRating")
  val maxRating: Int? = null,

  @SerialName("minTime")
  val minTime: Int? = null,
  @SerialName("maxTime")
  val maxTime: Int? = null,
  @SerialName("minServings")
  val minServings: Int? = null,
  @SerialName("maxServings")
  val maxServings: Int? = null,
  @SerialName("minCalories")
  val minCalories: Int? = null,
  @SerialName("maxCalories")
  val maxCalories: Int? = null,

  @SerialName("recipeLanguages")
  val recipeLanguages: List<String>? = null,
  @SerialName("userLanguage")
  val userLanguage: String? = null,
)

@Serializable
internal data class GetRecipesResponse(
  @SerialName("recipes")
  val recipes: List<RecipeInfoBody>,
  @SerialName("tags")
  val tags: Map<String, RecipeTagBody> = emptyMap(),
  @SerialName("categories")
  val categories: Map<String, RecipeCategoryInfoBody> = emptyMap(),
)

internal fun GetRecipesResponse.toEntity(): List<RecipeInfo> {
  val categoriesMap = categories.mapValues { entry ->
    Category(
      id = entry.key,
      name = entry.value.name,
      emoji = entry.value.emoji
    )
  }
  return recipes.map { it.toEntity(categoriesMap) }
}
