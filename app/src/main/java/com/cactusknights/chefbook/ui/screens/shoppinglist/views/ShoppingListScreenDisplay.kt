package com.cactusknights.chefbook.ui.screens.shoppinglist.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenEvent
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListState

@Composable
fun ShoppingListScreenDisplay(
    state: ShoppingListState,
    onEvent: (RecipeBookScreenEvent) -> Unit,
) {
    if (state is ShoppingListState.Success) {
        LazyColumn(
            modifier = Modifier
                .wrapContentHeight()
                .clip(RoundedCornerShape(28.dp, 28.dp, 0.dp, 0.dp)),
            verticalArrangement = Arrangement.Bottom,
        ) {
            repeat(2) {
                items(state.shoppingList) { section ->
                    ShoppingListSection(
                        state = section.copy(title = "Капкейки", recipeId = 12),
                        onPurchaseClicked = {},
                        onRecipeOpen = {},
                        onAddPurchase = {},
                    )
                }
            }
            item {
                ShoppingListSection(
                    state = state.shoppingList[0],
                    onPurchaseClicked = {},
                    onRecipeOpen = {},
                    onAddPurchase = {},
                )
            }
        }
    }
}
