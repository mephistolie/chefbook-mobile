package com.mysty.chefbook.features.recipe.input.ui.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.recipe.input.R
import com.mysty.chefbook.features.recipe.input.navigation.IRecipeInputDetailsScreenNavigator
import com.mysty.chefbook.features.recipe.input.navigation.handleBaseRecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.mysty.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import com.mysty.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import com.mysty.chefbook.ui.common.dialogs.LoadingDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import kotlinx.coroutines.flow.collectLatest

private const val CLOSE_RECIPE_INPUT_REQUEST = "CLOSE_RECIPE_INPUT"

@Destination(route = "details")
@Composable
fun RecipeInputDetailsScreen(
  viewModel: IRecipeInputScreenViewModel,
  navigator: IRecipeInputDetailsScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>
) {
  val state = viewModel.state.collectAsState()

  RecipeInputDetailsScreenContent(
    state = state.value.input,
    onIntent = viewModel::handleIntent,
    onDetailsIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Details(data)) }
  )
  if (state.value.isLoading) {
    LoadingDialog()
    BackHandler {}
  }

  confirmDialogRecipient.onNavResult { navResult ->
    if (navResult is NavResult.Value && navResult.value is TwoButtonsDialogResult.RightButtonClicked) {
      when (navResult.value.request) {
        CLOSE_RECIPE_INPUT_REQUEST -> navigator.navigateUp()
      }
    }
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collectLatest { effect ->
      when (effect) {
        is RecipeInputScreenEffect.Details.OnVisibilityPickerOpen -> navigator.openVisibilityDialog()
        is RecipeInputScreenEffect.Details.OnEncryptionStatePickerOpen -> navigator.openEncryptionStatePickerDialog()
        is RecipeInputScreenEffect.Details.OnEncryptedVaultMenuOpen -> navigator.openEncryptedVaultScreen()
        is RecipeInputScreenEffect.Details.OnLanguagePickerOpen -> navigator.openLanguageDialog()
        is RecipeInputScreenEffect.Details.OnCaloriesDialogOpen -> navigator.openCaloriesDialog()
        is RecipeInputScreenEffect.OnBack -> {
          if (state.value.input.name.isEmpty()) {
            navigator.navigateUp()
          } else {
            navigator.openTwoButtonsDialog(
              params = TwoButtonsDialogParams(
                descriptionId = R.string.common_recipe_input_screen_close_warning,
              ),
              request = CLOSE_RECIPE_INPUT_REQUEST,
            )
          }
        }
        is RecipeInputScreenEffect.OnContinue -> navigator.openRecipeInputIngredientScreen()
        is RecipeInputScreenEffect.OnClose -> navigator.closeRecipeInput(recipeId = effect.recipeId)
        else -> handleBaseRecipeInputScreenEffect(effect, navigator = navigator)
      }
    }
  }
}
