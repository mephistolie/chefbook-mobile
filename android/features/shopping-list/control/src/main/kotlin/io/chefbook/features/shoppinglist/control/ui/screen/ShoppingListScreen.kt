package io.chefbook.features.shoppinglist.control.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.shoppinglist.control.navigation.ShoppingListScreenNavigator
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenEffect
import io.chefbook.navigation.navigators.BaseNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(
  route = "shopping_list",
  deepLinks = [
    DeepLink(uriPattern = "https://chefbook.io/shopping-lists"),
    DeepLink(uriPattern = "https://chefbook.io/shopping-list"),
  ],
  style = DestinationStyleBottomSheet::class
)
fun ShoppingListScreen(
  navigator: ShoppingListScreenNavigator,
) {
  val viewModel = koinViewModel<ShoppingListScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  val scope = rememberCoroutineScope()

  val modalSheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true
  )

  ShoppingListScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
    modalSheetState = modalSheetState,
    shoppingListScreenNavigator = object : BaseNavigator {
      override fun navigateUp(skipAnimation: Boolean) {
        scope.launch { modalSheetState.hide() }
      }
    }
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ShoppingListScreenEffect.ModalSheetOpened -> modalSheetState.show()
        is ShoppingListScreenEffect.OnRecipeOpened -> navigator.openRecipeScreen(
          recipeId = effect.recipeId,
          openExpanded = true,
        )

        ShoppingListScreenEffect.Closed -> navigator.navigateUp()
      }
    }
  }
}
