package io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.dto

import io.chefbook.libs.models.language.LanguageMapper
import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.ProfileBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.RatingBody
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.VisibilitySerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RecipeInfoBody(
  @SerialName("id")
  val id: String,
  @SerialName("name")
  val name: String,

  @SerialName("owner")
  val owner: ProfileBody,

  @SerialName("owned")
  val isOwned: Boolean,
  @SerialName("saved")
  val isSaved: Boolean = false,
  @SerialName("visibility")
  val visibility: VisibilitySerializable? = null,
  @SerialName("encrypted")
  val isEncrypted: Boolean = false,

  @SerialName("language")
  val language: String,
  @SerialName("preview")
  val preview: String? = null,

  @SerialName("creationTimestamp")
  val creationTimestamp: String,
  @SerialName("updateTimestamp")
  val updateTimestamp: String,
  @SerialName("version")
  val version: Int,

  @SerialName("rating")
  val rating: RatingBody? = null,

  @SerialName("tags")
  val tags: List<String> = emptyList(),
  @SerialName("categories")
  val categories: List<String> = emptyList(),
  @SerialName("favourite")
  val isFavourite: Boolean = false,

  @SerialName("servings")
  val servings: Int? = null,
  @SerialName("time")
  val time: Int? = null,

  @SerialName("calories")
  val calories: Int? = null,
)

internal fun RecipeInfoBody.toEntity(categoriesMap: Map<String, Category>): DecryptedRecipeInfo {
  val meta = RecipeMeta(
    id = id,
    owner = ProfileInfo(
      id = owner.id,
      name = owner.name,
      avatar = owner.avatar,
    ),
    visibility = when (visibility) {
      VisibilitySerializable.PUBLIC -> RecipeMeta.Visibility.PUBLIC
      VisibilitySerializable.LINK -> RecipeMeta.Visibility.LINK
      else -> RecipeMeta.Visibility.PRIVATE
    },
    isEncryptionEnabled = isEncrypted,
    language = LanguageMapper.map(language),
    version = version,
    creationTimestamp = creationTimestamp,
    updateTimestamp = updateTimestamp,
    rating = RecipeMeta.Rating(
      index = rating?.index ?: 0F,
      score = rating?.score,
      votes = rating?.votes ?: 0,
    )
  )

  return DecryptedRecipeInfo(
    meta = meta,
    name = name,
    isOwned = isOwned,
    isSaved = isSaved,
    preview = preview,
    categories = categories.mapNotNull(categoriesMap::get),
    isFavourite = isFavourite,
    servings = servings,
    time = time,
    calories = calories,
  )
}
