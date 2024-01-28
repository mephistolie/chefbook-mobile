package io.chefbook.sdk.category.api.external.domain.entities

data class Category(
  val id: String,
  val name: String,
  val emoji: String? = null,
)
