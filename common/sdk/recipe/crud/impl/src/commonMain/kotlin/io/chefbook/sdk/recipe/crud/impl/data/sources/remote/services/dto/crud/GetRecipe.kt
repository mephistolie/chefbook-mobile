package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeCategoryInfoBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeTagBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class GetRecipeResponse(
  @SerialName("recipe")
  val recipe: RecipeBody,
  @SerialName("tags")
  val tags: Map<String, RecipeTagBody>,
  @SerialName("categories")
  val categories: Map<String, RecipeCategoryInfoBody>,
)

internal fun GetRecipeResponse.toEntity(): Recipe {
  val categoriesMap = categories.mapValues { entry ->
    Category(
      id = entry.key,
      name = entry.value.name,
      emoji = entry.value.emoji
    )
  }
  return recipe.toEntity(categoriesMap)
}
