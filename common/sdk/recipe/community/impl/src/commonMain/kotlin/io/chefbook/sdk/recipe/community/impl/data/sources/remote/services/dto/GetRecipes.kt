package io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.community.api.external.domain.entities.RecipesSorting
import io.chefbook.sdk.recipe.community.impl.data.models.RecipesQuery
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeCategoryInfoBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeTagBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRecipesResponse(
  @SerialName("recipes")
  val recipes: List<RecipeInfoBody>,
  @SerialName("tags")
  val tags: Map<String, RecipeTagBody> = emptyMap(),
  @SerialName("categories")
  val categories: Map<String, RecipeCategoryInfoBody> = emptyMap(),
)

internal fun GetRecipesResponse.toEntity(): List<DecryptedRecipeInfo> {
  val categoriesMap = categories.mapValues { entry ->
    Category(
      id = entry.key,
      name = entry.value.name,
      emoji = entry.value.emoji
    )
  }
  return recipes.map { it.toEntity(categoriesMap) }
}
