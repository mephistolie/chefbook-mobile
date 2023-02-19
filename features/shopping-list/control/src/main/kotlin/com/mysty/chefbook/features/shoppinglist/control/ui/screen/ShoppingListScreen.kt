package com.mysty.chefbook.features.shoppinglist.control.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenEffect
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.navigation.IShoppingListScreenNavigator
import org.koin.androidx.compose.getViewModel

@Composable
fun ShoppingListScreen(
  navigator: IShoppingListScreenNavigator,
) {
  val viewModel: IShoppingListScreenViewModel = getViewModel<ShoppingListScreenViewModel>()
  val state = viewModel.state.collectAsState()

  ShoppingListScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ShoppingListScreenEffect.OnPurchaseInputOpen -> navigator.openPurchaseInput(effect.purchaseId)
        is ShoppingListScreenEffect.OnRecipeOpened -> navigator.openRecipeScreen(
          recipeId = effect.recipeId,
          openExpanded = true,
        )
      }
    }
  }
}
