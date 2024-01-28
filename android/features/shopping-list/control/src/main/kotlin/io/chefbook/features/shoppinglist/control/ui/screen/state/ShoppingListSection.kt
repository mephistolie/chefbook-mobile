package io.chefbook.features.shoppinglist.control.ui.screen.state

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase

internal data class ShoppingListSection(
  val purchases: List<Purchase>,
  val title: String? = null,
  val recipeId: String? = null,
)
