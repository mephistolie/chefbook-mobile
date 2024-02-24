package io.chefbook.features.shoppinglist.control.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.modifiers.shimmer
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.ModalBottomSheetShape
import io.chefbook.features.shoppinglist.control.ui.screen.components.ShoppingListActionBar
import io.chefbook.features.shoppinglist.control.ui.screen.components.ShoppingListSelectorBar
import io.chefbook.features.shoppinglist.control.ui.screen.components.emptyListBanner
import io.chefbook.features.shoppinglist.control.ui.screen.components.shoppingListActionBarHeight
import io.chefbook.features.shoppinglist.control.ui.screen.components.shoppingListPurchases
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ModalState
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenIntent
import io.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenState
import io.chefbook.features.shoppinglist.purchases.input.ui.PurchaseInputDialog
import io.chefbook.navigation.navigators.BaseNavigator

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ShoppingListScreenContent(
  state: ShoppingListScreenState,
  onIntent: (ShoppingListScreenIntent) -> Unit,
  modalSheetState: ModalBottomSheetState,
  shoppingListScreenNavigator: BaseNavigator,
) {
  val colors = LocalTheme.colors

  ModalBottomSheetLayout(
    sheetState = modalSheetState,
    sheetBackgroundColor = Color.Transparent,
    sheetElevation = 0.dp,
    sheetContent = {
      Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.BottomCenter
      ) {
        if (modalSheetState.isVisible) {
          (state as? ShoppingListScreenState.Loaded)?.modalState?.let { modalState ->
            when (modalState) {
              is ModalState.None -> Unit
              is ModalState.PurchaseInput -> PurchaseInputDialog(
                shoppingListId = modalState.shoppingListId,
                purchaseId = modalState.purchaseId,
                navigator = shoppingListScreenNavigator,
              )
            }
          }
        }
      }
    }
  ) {
    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
      Box(
        modifier = Modifier
          .statusBarsPadding()
          .wrapContentHeight()
          .clippedBackground(colors.divider, shape = ModalBottomSheetShape)
          .shimmer(isEnabled = state is ShoppingListScreenState.Loading)
          .animateContentSize(),
      ) {
        if (state is ShoppingListScreenState.Loaded) {
          LazyColumn(
            modifier = Modifier
              .navigationBarsPadding()
              .wrapContentHeight()
              .animateContentSize(),
            verticalArrangement = Arrangement.Bottom,
          ) {
            shoppingListPurchases(
              shoppingList = state.sections,
              onTitleClick = { recipeId -> onIntent(ShoppingListScreenIntent.OpenRecipe(recipeId)) },
              onPurchaseClick = { purchaseId ->
                onIntent(ShoppingListScreenIntent.SwitchPurchasedStatus(purchaseId))
              },
              onEditPurchaseClick = { purchaseId ->
                onIntent(ShoppingListScreenIntent.OpenPurchaseInput(purchaseId))
              }
            )
            if (state.sections.isEmpty()) emptyListBanner()
            item { Spacer(modifier = Modifier.height(shoppingListActionBarHeight + 8.dp)) }
          }
        }
        ShoppingListSelectorBar(
          title = state.title,
          onSelectClick = {},
          onCloseClick = { onIntent(ShoppingListScreenIntent.Close) },
          modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = shoppingListActionBarHeight + 8.dp)
        )
        ShoppingListActionBar(
          onAddPurchaseClick = { onIntent(ShoppingListScreenIntent.CreatePurchase) },
          onDoneClick = { onIntent(ShoppingListScreenIntent.RemovePurchasedItems) },
          isDoneButtonActive = (state as? ShoppingListScreenState.Loaded)?.isDoneButtonEnabled
            ?: false,
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .wrapContentHeight()
        )
      }
    }
  }
}
