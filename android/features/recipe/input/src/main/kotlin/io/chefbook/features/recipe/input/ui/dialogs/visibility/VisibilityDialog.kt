package io.chefbook.features.recipe.input.ui.dialogs.visibility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import io.chefbook.navigation.navigators.BaseNavigator

@Destination(
  route = "visibility",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun VisibilityDialog(
  viewModel: IRecipeInputScreenViewModel,
  navigator: BaseNavigator,
) {
  val state = viewModel.state.collectAsState()

  VisibilityDialogContent(
    selectedVisibility = state.value.input.visibility,
    onIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Details(data)) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is RecipeInputScreenEffect.OnBottomSheetClosed) navigator.navigateUp()
    }
  }
}
