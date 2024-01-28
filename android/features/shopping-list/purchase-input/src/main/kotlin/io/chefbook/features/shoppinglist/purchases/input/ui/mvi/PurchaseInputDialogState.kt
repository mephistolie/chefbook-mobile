package io.chefbook.features.shoppinglist.purchases.input.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase

data class PurchaseInputDialogState(
  val purchase: Purchase = Purchase(id = "", name = ""),
) : MviState
