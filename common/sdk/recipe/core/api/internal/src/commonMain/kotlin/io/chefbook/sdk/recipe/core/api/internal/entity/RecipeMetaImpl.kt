package io.chefbook.sdk.recipe.core.api.internal.entity

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.libs.models.visibility.Visibility
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

data class RecipeMetaImpl(
  override val id: String,

  override val owner: ProfileInfo,

  override val visibility: Visibility = Visibility.PRIVATE,
  override val isEncryptionEnabled: Boolean = false,

  override val language: Language,

  override val version: Int,
  override val creationTimestamp: String? = null,
  override val updateTimestamp: String? = null,

  override val rating: RecipeMeta.Rating = RecipeMeta.Rating(),

  override val tags: List<Tag>,
) : RecipeMeta {

  override fun withId(id: String) = copy(id = id)
  override fun withScore(score: Int?) = copy(rating = rating.withScore(score))
  override fun withVersion(version: Int) = copy(version = version)
}