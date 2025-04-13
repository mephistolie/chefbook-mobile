package io.chefbook.sdk.collection.api.external.domain.entities

import io.chefbook.libs.models.visibility.Visibility

data class Collection(
  val id: String,
  val name: String,
  val visibility: Visibility,
  val contributors: List<Contributor>,
  val recipesCount: Int,
)
