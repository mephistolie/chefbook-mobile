package io.chefbook.features.recipe.input.ui.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import io.chefbook.features.recipe.input.R
import io.chefbook.features.recipe.input.navigation.RecipeInputDetailsScreenNavigator
import io.chefbook.features.recipe.input.navigation.handleBaseRecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import io.chefbook.ui.common.dialogs.LoadingDialog
import kotlinx.coroutines.flow.collectLatest

private const val CLOSE_RECIPE_INPUT_REQUEST = "CLOSE_RECIPE_INPUT"

@Destination(
  route = "details",
  deepLinks = [DeepLink(uriPattern = "https://chefbook.io/recipes/create")],
)
@Composable
fun RecipeInputDetailsScreen(
  viewModel: IRecipeInputScreenViewModel,
  navigator: RecipeInputDetailsScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>
) {
  val state = viewModel.state.collectAsState()

  RecipeInputDetailsScreenContent(
    state = state.value,
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
