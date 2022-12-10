package com.cactusknights.chefbook.ui.screens.shoppinglist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.cactusknights.chefbook.ui.screens.shoppinglist.views.ShoppingListScreenDisplay

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    ShoppingListScreenDisplay(
        state = state.value,
        onEvent = {},
    )
}
