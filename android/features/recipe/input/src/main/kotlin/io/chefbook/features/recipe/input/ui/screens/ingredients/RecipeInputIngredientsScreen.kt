package io.chefbook.features.recipe.input.ui.screens.ingredients

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.recipe.input.navigation.RecipeInputIngredientsScreenNavigator
import io.chefbook.features.recipe.input.navigation.handleBaseRecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import io.chefbook.ui.common.dialogs.LoadingDialog

@Destination(route = "ingredients")
@Composable
internal fun RecipeInputIngredientScreen(
  viewModel: RecipeInputScreenViewModel,
  navigator: RecipeInputIngredientsScreenNavigator,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

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

