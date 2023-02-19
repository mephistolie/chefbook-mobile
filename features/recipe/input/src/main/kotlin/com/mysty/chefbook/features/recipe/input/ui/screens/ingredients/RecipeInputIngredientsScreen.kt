package com.mysty.chefbook.features.recipe.input.ui.screens.ingredients

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.recipe.input.navigation.IRecipeInputIngredientsScreenNavigator
import com.mysty.chefbook.features.recipe.input.navigation.handleBaseRecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.mysty.chefbook.ui.common.dialogs.LoadingDialog
import com.ramcosta.composedestinations.annotation.Destination

@Destination(route = "ingredients")
@Composable
internal fun RecipeInputIngredientScreen(
  viewModel: IRecipeInputScreenViewModel,
  navigator: IRecipeInputIngredientsScreenNavigator,
) {
  val state = viewModel.state.collectAsState()

  RecipeInputIngredientScreenContent(
    state = state.value.input,
    onIntent = viewModel::handleIntent,
    onIngredientsIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Ingredients(data)) },
  )
  if (state.value.isLoading) {
    LoadingDialog()
    BackHandler {}
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeInputScreenEffect.Ingredients.OnDialogOpen -> navigator.openIngredientDialog(effect.ingredientId)
        is RecipeInputScreenEffect.OnContinue -> navigator.openRecipeInputCookingScreen()
        else -> handleBaseRecipeInputScreenEffect(effect, navigator = navigator)
      }
    }
  }
}
