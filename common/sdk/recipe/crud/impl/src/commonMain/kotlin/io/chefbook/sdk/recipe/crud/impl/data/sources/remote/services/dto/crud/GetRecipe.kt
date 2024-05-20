package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeCategoryInfoBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeTagBody
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.entities.TagGroup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class GetRecipeResponse(
  @SerialName("recipe")
  val recipe: RecipeBody,
  @SerialName("tags")
  val tags: Map<String, RecipeTagBody>? = null,
  @SerialName("tagGroups")
  val tagGroups: Map<String, String>? = null,
  @SerialName("categories")
  val categories: Map<String, RecipeCategoryInfoBody>? = null,
)

internal fun GetRecipeResponse.toEntity(): Recipe {
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
      emoji = entry.value.emoji,
      group = entry.value.groupId?.let { groupId ->
        tagGroups.orEmpty()[groupId]?.let { groupName ->
          TagGroup(
            id = groupId,
            name = groupName,
          )
        }
      }
    )
  }
  return recipe.toEntity(categoriesMap = categoriesMap, tagsMap = tagsMap)
}
