package io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto

import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.book.api.internal.data.models.RecipeState
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RatingBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RecipeStateBody(
  @SerialName("id")
  val id: String,

  @SerialName("owner")
  val owner: OwnerInfo? = null,

  @SerialName("version")
  val version: Int,

  @SerialName("rating")
  val rating: RatingBody? = null,

  @SerialName("categories")
  val categories: List<String> = emptyList(),
  @SerialName("favourite")
  val isFavourite: Boolean = false,
) {
  @Serializable
  class OwnerInfo(
    @SerialName("name")
    val name: String? = null,
    @SerialName("avatar")
    val avatar: String? = null,
  )
}

internal fun RecipeStateBody.toModel(categoriesMap: Map<String, Category>): RecipeState =
  RecipeState(
    id = id,
    ownerName = owner?.name,
    ownerAvatar = owner?.avatar,
    version = version,
    rating = RecipeMeta.Rating(
      index = rating?.index ?: 0F,
      score = rating?.score,
      votes = rating?.votes ?: 0,
    ),
    categories = categories.mapNotNull(categoriesMap::get),
    isFavourite = isFavourite,
  )
