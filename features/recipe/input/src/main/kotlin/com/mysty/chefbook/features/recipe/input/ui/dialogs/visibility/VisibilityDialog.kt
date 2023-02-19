package com.mysty.chefbook.features.recipe.input.ui.dialogs.visibility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle

@Destination(
  route = "visibility",
  style = DestinationStyle.BottomSheet::class,
)
@Composable
internal fun VisibilityDialog(
  viewModel: IRecipeInputScreenViewModel,
  navigator: IBaseNavigator,
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
