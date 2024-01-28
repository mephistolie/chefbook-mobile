package io.chefbook.features.recipe.control.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenEffect
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenIntent
import io.chefbook.features.recipe.control.navigation.RecipeControlScreenNavigator
import io.chefbook.features.recipe.control.ui.state.RecipeControlScreenPage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.core.android.showToast
import io.chefbook.features.recipe.control.R
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

private const val REMOVE_FROM_RECIPE_BOOK_REQUEST = "REMOVE_FROM_RECIPE_BOOK"
private const val DELETE_RECIPE_REQUEST = "DELETE_RECIPE"

@Destination(
  route = "control",
  style = DestinationStyleBottomSheet::class
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeControlScreen(
  recipeId: String,
  navigator: RecipeControlScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>
) {
  val viewModel: IRecipeControlScreenViewModel =
    getViewModel<RecipeControlScreenViewModel> { parametersOf(recipeId) }
  val state = viewModel.state.collectAsState()

  val pages = remember { RecipeControlScreenPage.entries.toTypedArray() }
  val pagerState = rememberPagerState { pages.size }

  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  RecipeControlScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
    pagerState = pagerState,
  )

  BackHandler {
    if (pagerState.currentPage == pages.indexOf(RecipeControlScreenPage.CATEGORIES_SELECTION)) {
      scope.launch {
        pagerState.animateScrollToPage(pages.indexOf(RecipeControlScreenPage.MENU))
      }
    } else {
      viewModel.handleIntent(RecipeControlScreenIntent.Close)
    }
  }

  confirmDialogRecipient.onNavResult { navResult ->
    if (navResult is NavResult.Value && navResult.value is TwoButtonsDialogResult.RightButtonClicked) {
      when (navResult.value.request) {
        REMOVE_FROM_RECIPE_BOOK_REQUEST -> viewModel.handleIntent(RecipeControlScreenIntent.ChangeSavedStatus)
        DELETE_RECIPE_REQUEST -> viewModel.handleIntent(RecipeControlScreenIntent.DeleteRecipe)
      }
    }
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeControlScreenEffect.OpenRemoveFromRecipeBookConfirmDialog -> {
          navigator.openTwoButtonsDialog(
            params = TwoButtonsDialogParams(
              descriptionId = R.string.common_recipe_control_screen_remove_from_recipe_book_warning
            ),
            request = REMOVE_FROM_RECIPE_BOOK_REQUEST
          )
        }

        is RecipeControlScreenEffect.OpenCategoriesSelectionPage -> {
          pagerState.animateScrollToPage(pages.indexOf(RecipeControlScreenPage.CATEGORIES_SELECTION))
        }

        is RecipeControlScreenEffect.EditRecipe -> navigator.openRecipeInputScreen(recipeId)
        is RecipeControlScreenEffect.OpenDeleteRecipeConfirmDialog -> {
          navigator.openTwoButtonsDialog(
            params = TwoButtonsDialogParams(
              descriptionId = R.string.common_recipe_control_screen_delete_warning
            ),
            request = DELETE_RECIPE_REQUEST
          )
        }

        is RecipeControlScreenEffect.ShowToast -> context.showToast(effect.messageId)
        is RecipeControlScreenEffect.Close -> navigator.navigateUp()
      }
    }
  }
}