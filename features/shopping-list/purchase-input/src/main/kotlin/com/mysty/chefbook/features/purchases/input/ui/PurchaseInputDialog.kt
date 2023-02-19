package com.mysty.chefbook.features.purchases.input.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.purchases.input.ui.mvi.PurchaseInputDialogEffect
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Destination(
  route = "purchase-input",
  style = DestinationStyle.BottomSheet::class,
)
@Composable
internal fun PurchaseInputDialog(
  ingredientId: String,
  navigator: IBaseNavigator,
) {
  val viewModel: IPurchaseInputDialogViewModel = getViewModel<PurchaseInputDialogViewModel> { parametersOf(ingredientId) }
  val state = viewModel.state.collectAsState()

  PurchaseInputDialogContent(
    state = state.value.purchase,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is PurchaseInputDialogEffect.Close) navigator.navigateUp()
    }
  }
}
