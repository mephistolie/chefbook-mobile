package com.mysty.chefbook.features.purchases.input.ui.mvi

import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.core.android.mvi.MviState
import com.mysty.chefbook.core.constants.Strings

data class PurchaseInputDialogState(
  val purchase: Purchase = Purchase(id = Strings.EMPTY, name = Strings.EMPTY),
) : MviState
