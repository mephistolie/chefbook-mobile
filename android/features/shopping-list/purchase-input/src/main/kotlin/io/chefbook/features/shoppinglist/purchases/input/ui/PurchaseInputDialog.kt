package io.chefbook.features.shoppinglist.purchases.input.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.chefbook.features.shoppinglist.purchases.input.ui.mvi.PurchaseInputDialogEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.shoppinglist.purchases.input.ui.mvi.PurchaseInputDialogIntent
import io.chefbook.navigation.navigators.BaseNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Destination(
  route = "purchase-input",
  style = DestinationStyleBottomSheet::class,
)
@Composable
fun PurchaseInputDialog(
  shoppingListId: String,
  purchaseId: String,
  navigator: BaseNavigator,
) {
  val viewModel: IPurchaseInputDialogViewModel =
    getViewModel<PurchaseInputDialogViewModel> { parametersOf(shoppingListId, purchaseId) }
  val state = viewModel.state.collectAsState()

  PurchaseInputDialogContent(
    state = state.value.purchase,
    onIntent = viewModel::handleIntent,
  )

  BackHandler { viewModel.handleIntent(PurchaseInputDialogIntent.Close) }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is PurchaseInputDialogEffect.Closed) navigator.navigateUp()
    }
  }
}
