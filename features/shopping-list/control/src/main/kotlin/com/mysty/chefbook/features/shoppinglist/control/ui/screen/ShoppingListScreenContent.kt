package com.mysty.chefbook.features.shoppinglist.control.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.components.ShoppingListActionBar
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.components.shoppingListPurchases
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenIntent
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenState
import com.mysty.chefbook.ui.common.providers.LocalBottomSheetExpandProgressProvider
import timber.log.Timber

/* TODO: remove hardcode offset logic & optimize bottomsheet when
 * Google fix standard solution
 */
private val TOP_BAR_HEIGHT = 56.dp

@Composable
internal fun ShoppingListScreenContent(
  state: ShoppingListScreenState,
  onIntent: (ShoppingListScreenIntent) -> Unit,
) {
  val density = LocalDensity.current
  val screenHeight =  with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
  var contentHeight by remember { mutableStateOf(0) }
  val expandProgress = LocalBottomSheetExpandProgressProvider.current.expandProgress

  Timber.e("Tester $screenHeight $contentHeight")
  if (state is ShoppingListScreenState.Success) {
    Column(
      modifier = Modifier
        .onGloballyPositioned { coordinates ->
          contentHeight = coordinates.size.height
        }
    ) {
      LazyColumn(
        modifier = Modifier
          .weight(1F, false)
          .wrapContentHeight()
          .padding(horizontal = 6.dp)
          .animateContentSize(),
        verticalArrangement = Arrangement.Bottom,
      ) {
        shoppingListPurchases(
          shoppingList = state.shoppingList,
          onTitleClick = { recipeId -> onIntent(ShoppingListScreenIntent.OpenRecipe(recipeId)) },
          onPurchaseClick = { purchaseId ->
            onIntent(ShoppingListScreenIntent.SwitchPurchasedStatus(purchaseId))
          },
          onEditPurchaseClick = { purchaseId ->
            onIntent(ShoppingListScreenIntent.OpenPurchaseInput(purchaseId))
          }
        )
      }
      ShoppingListActionBar(
        onAddPurchaseClick = { onIntent(ShoppingListScreenIntent.CreatePurchase) },
        onDoneClick = { onIntent(ShoppingListScreenIntent.RemovePurchasedItems) },
        isDoneButtonActive = state.hasPurchasedItems,
        modifier = Modifier
          .wrapContentHeight()
          .offset(
            y = if (contentHeight > screenHeight) {
              -(TOP_BAR_HEIGHT - TOP_BAR_HEIGHT * expandProgress)
            } else 0.dp
          )
      )
    }
  }
}
