package com.mysty.chefbook.features.purchases.input.ui.mvi

import com.mysty.chefbook.api.common.entities.unit.MeasureUnit
import com.mysty.chefbook.core.android.mvi.MviIntent

sealed class PurchaseInputDialogIntent : MviIntent {
  data class SetName(val name: String) : PurchaseInputDialogIntent()
  data class SetAmount(val amount: Int?) : PurchaseInputDialogIntent()
  data class SetMeasureUnit(val unit: MeasureUnit?) : PurchaseInputDialogIntent()

  object Close : PurchaseInputDialogIntent()
}
