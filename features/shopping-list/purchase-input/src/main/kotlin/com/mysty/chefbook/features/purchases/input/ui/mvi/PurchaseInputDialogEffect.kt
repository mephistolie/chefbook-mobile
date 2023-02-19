package com.mysty.chefbook.features.purchases.input.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

sealed class PurchaseInputDialogEffect : MviSideEffect {
  object Close : PurchaseInputDialogEffect()
}
