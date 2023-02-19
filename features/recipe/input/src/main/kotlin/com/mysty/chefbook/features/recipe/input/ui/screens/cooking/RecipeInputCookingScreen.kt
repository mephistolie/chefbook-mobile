package com.mysty.chefbook.features.recipe.input.ui.screens.cooking

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.recipe.input.navigation.IRecipeInputScreenBaseNavigator
import com.mysty.chefbook.features.recipe.input.navigation.handleBaseRecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.mysty.chefbook.ui.common.dialogs.LoadingDialog
import com.ramcosta.composedestinations.annotation.Destination

@Destination(route = "cooking")
@Composable
internal fun RecipeInputCookingScreen(
  viewModel: IRecipeInputScreenViewModel,
  navigator: IRecipeInputScreenBaseNavigator,
) {
  val state = viewModel.state.collectAsState()

  RecipeInputCookingScreenDisplay(
    state = state.value.input,
    onIntent = viewModel::handleIntent,
    onCookingIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Cooking(data)) }
  )
  if (state.value.isLoading) {
    LoadingDialog()
    BackHandler {}
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeInputScreenEffect.OnBack -> navigator.navigateUp()
        is RecipeInputScreenEffect.OnClose -> navigator.closeRecipeInput(recipeId = effect.recipeId)
        else -> handleBaseRecipeInputScreenEffect(effect, navigator = navigator)
      }
    }
  }
}
