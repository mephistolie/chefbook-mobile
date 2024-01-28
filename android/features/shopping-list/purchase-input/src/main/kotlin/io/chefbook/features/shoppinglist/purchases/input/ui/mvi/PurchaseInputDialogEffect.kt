package io.chefbook.features.shoppinglist.purchases.input.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

sealed class PurchaseInputDialogEffect : MviSideEffect {
  data object Closed : PurchaseInputDialogEffect()
}
