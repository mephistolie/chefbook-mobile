package io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.book.api.internal.data.models.RecipeBookState
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RecipeCategoryBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetRecipeBookResponse(
  @SerialName("recipes")
  val recipes: List<RecipeStateBody>,
  @SerialName("categories")
  val categories: List<RecipeCategoryBody> = emptyList(),
  @SerialName("isEncryptedVaultEnabled")
  val isEncryptedVaultEnabled: Boolean? = false,
)

internal fun GetRecipeBookResponse.toModel(): RecipeBookState {
  val categories = categories.map {
    Category(
      id = it.id,
      name = it.name,
      emoji = it.emoji
    )
  }
  val categoriesMap = categories.associateBy { it.id }
  return RecipeBookState(
    recipes = recipes.map { it.toModel(categoriesMap) },
    categories = categories,
    isEncryptedVaultEnabled = isEncryptedVaultEnabled ?: false,
  )
}
