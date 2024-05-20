package io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeCategoryInfoBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeTagBody
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRecipesResponse(
  @SerialName("recipes")
  val recipes: List<RecipeInfoBody>,
  @SerialName("categories")
  val categories: Map<String, RecipeCategoryInfoBody>? = null,
  @SerialName("tags")
  val tags: Map<String, RecipeTagBody>? = null,
  @SerialName("tagGroups")
  val tagGroups: Map<String, String>? = null,
)

internal fun GetRecipesResponse.toEntity(): List<DecryptedRecipeInfo> {
  val categoriesMap = categories.orEmpty().mapValues { entry ->
    Category(
      id = entry.key,
      name = entry.value.name,
      emoji = entry.value.emoji
    )
  }
  val tagsMap = tags.orEmpty().mapValues { entry ->
    Tag(
      id = entry.key,
      name = entry.value.name,
      emoji = entry.value.emoji
    )
  }
  return recipes.map { recipe ->
    recipe.toEntity(
      categoriesMap = categoriesMap,
      tagsMap = tagsMap,
    )
  }
}
