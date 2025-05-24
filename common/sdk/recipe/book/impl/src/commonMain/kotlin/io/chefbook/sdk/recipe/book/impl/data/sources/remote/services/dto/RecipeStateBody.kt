package io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.dto

import io.chefbook.sdk.recipe.book.api.internal.data.models.RecipeState
import io.chefbook.sdk.recipe.core.api.external.domain.entities.CollectionInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RatingBody
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RecipeStateBody(
  @SerialName("id")
  val id: String,

  @SerialName("version")
  val version: Int,

  @SerialName("rating")
  val rating: RatingBody? = null,

  @SerialName("tags")
  val tags: List<String> = emptyList(),
  @SerialName("collections")
  val collections: List<String> = emptyList(),
  @SerialName("favourite")
  val isFavourite: Boolean = false,
)

internal fun RecipeStateBody.toModel(
  collectionsMap: Map<String, CollectionInfo>,
  tagsGroup: Map<String, Tag>,
): RecipeState =
  RecipeState(
    id = id,
    version = version,
    rating = RecipeMeta.Rating(
      index = rating?.index ?: 0F,
      score = rating?.score,
      votes = rating?.votes ?: 0,
    ),
    collections = collections.mapNotNull(collectionsMap::get),
    tags = tags.mapNotNull(tagsGroup::get),
    isFavourite = isFavourite,
  )
