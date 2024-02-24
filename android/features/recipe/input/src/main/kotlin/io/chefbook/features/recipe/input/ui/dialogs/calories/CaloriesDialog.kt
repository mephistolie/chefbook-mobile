package io.chefbook.features.recipe.input.ui.dialogs.calories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import io.chefbook.navigation.navigators.BaseNavigator

@Destination(
  route = "calories",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun CaloriesDialog(
  viewModel: IRecipeInputScreenViewModel,
  navigator: BaseNavigator,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

  CaloriesDialogContent(
    state = state.value.input,
    onIntent = viewModel::handleIntent,
    onDetailsIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Details(data)) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is RecipeInputScreenEffect.OnBottomSheetClosed) navigator.navigateUp()
    }
  }
}