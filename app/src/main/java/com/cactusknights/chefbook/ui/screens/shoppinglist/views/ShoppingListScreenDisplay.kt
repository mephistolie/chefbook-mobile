package com.cactusknights.chefbook.ui.screens.shoppinglist.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListScreenEvent
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListState
import com.mysty.chefbook.design.theme.shapes.BottomSheetShape

@Composable
fun ShoppingListScreenDisplay(
    state: ShoppingListState,
    onEvent: (ShoppingListScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state is ShoppingListState.Success) {
        Column(
            modifier = modifier,
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1F, false)
                    .wrapContentHeight()
                    .padding(horizontal = 6.dp)
                    .clip(BottomSheetShape)
                    .animateContentSize(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                itemsIndexed(state.shoppingList) { index, section ->
                    ShoppingListSection(
                        state = section,
                        onPurchaseClicked = { purchaseId ->
                            onEvent(ShoppingListScreenEvent.SwitchPurchasedStatus(purchaseId))
                        },
                        onRecipeOpen = { recipeId -> onEvent(ShoppingListScreenEvent.OpenRecipe(recipeId)) },
                        modifier = Modifier.padding(bottom = if (index < state.shoppingList.lastIndex) 6.dp else 0.dp)
                    )
                }
            }
            ActionBar(
                onAddPurchaseClick = {},
                onDoneClick = { onEvent(ShoppingListScreenEvent.RemovePurchasedItems) },
                isDoneButtonActive = state.hasPurchasedItems,
                modifier = Modifier.wrapContentHeight()
            )
        }
    }
}
