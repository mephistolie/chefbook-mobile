package io.chefbook.features.shoppinglist.control.ui.screen.mvi

import io.chefbook.features.shoppinglist.control.ui.screen.state.ShoppingListSection
import io.chefbook.libs.mvi.MviState

internal sealed interface ShoppingListScreenState : MviState {

  val title: String?

  data class Loading(override val title: String? = null) : ShoppingListScreenState

  data class Loaded(
    override val title: String,
    val sections: List<ShoppingListSection>,
    val isDoneButtonEnabled: Boolean,
    val modalState: ModalState = ModalState.None
  ) : ShoppingListScreenState

  data class Error(override val title: String? = null) : ShoppingListScreenState
}

internal sealed interface ModalState {

  data object None : ModalState

  data class PurchaseInput(
    val shoppingListId: String,
    val purchaseId: String
  ) : ModalState
}
