package com.cactusknights.chefbook.ui.screens.shoppinglist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListEffect
import com.cactusknights.chefbook.ui.screens.shoppinglist.views.ShoppingListScreenDisplay

@Composable
fun ShoppingListScreen(
    onOpenRecipe: (String) -> Unit,
    viewModel: ShoppingListScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    ShoppingListScreenDisplay(
        state = state.value,
        onEvent = viewModel::obtainEvent,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                is ShoppingListEffect.OnRecipeOpened -> {
                    onOpenRecipe(effect.recipeId)
                }
            }
        }
    }
}
