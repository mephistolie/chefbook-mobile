package io.chefbook.features.shoppinglist.purchases.input.ui.mvi

import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.libs.mvi.MviIntent

sealed class PurchaseInputDialogIntent : MviIntent {
  data class SetName(val name: String) : PurchaseInputDialogIntent()
  data class SetAmount(val amount: Int?) : PurchaseInputDialogIntent()
  data class SetMeasureUnit(val unit: MeasureUnit?) : PurchaseInputDialogIntent()

  data object Close : PurchaseInputDialogIntent()
}
