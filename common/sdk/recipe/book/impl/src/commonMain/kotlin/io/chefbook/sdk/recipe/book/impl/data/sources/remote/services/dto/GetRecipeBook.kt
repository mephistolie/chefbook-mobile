package io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.book.api.internal.data.models.RecipeBookState
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeCategoryBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeTagBody
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.entities.TagGroup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRecipeBookResponse(
  @SerialName("recipes")
  val recipes: List<RecipeStateBody>,
  @SerialName("categories")
  val categories: List<RecipeCategoryBody>? = null,
  @SerialName("tags")
  val tags: Map<String, RecipeTagBody>? = null,
  @SerialName("tagGroups")
  val tagGroups: Map<String, String>? = null,
  @SerialName("isEncryptedVaultEnabled")
  val isEncryptedVaultEnabled: Boolean? = false,
)

internal fun GetRecipeBookResponse.toModel(): RecipeBookState {
  val categories = categories.orEmpty().map {
    Category(
      id = it.id,
      name = it.name,
      emoji = it.emoji
    )
  }
  val categoriesMap = categories.associateBy { it.id }
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
  return RecipeBookState(
    recipes = recipes.map { it.toModel(categoriesMap, tagsMap) },
    categories = categories,
    isEncryptedVaultEnabled = isEncryptedVaultEnabled ?: false,
  )
}
