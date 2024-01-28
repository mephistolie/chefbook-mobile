package io.chefbook.sdk.shoppinglist.api.external.domain.entities

import io.chefbook.libs.models.measureunit.MeasureUnit

data class Purchase(
  val id: String,
  val name: String,
  val amount: Int? = null,
  val measureUnit: MeasureUnit? = null,
  val multiplier: Int = 1,
  val isPurchased: Boolean = false,
  val recipeId: String? = null,
)
