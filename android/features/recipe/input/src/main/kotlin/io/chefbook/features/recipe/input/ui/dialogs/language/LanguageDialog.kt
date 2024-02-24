package io.chefbook.features.recipe.input.ui.dialogs.language

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
  route = "language",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun LanguageDialog(
  viewModel: IRecipeInputScreenViewModel,
  navigator: BaseNavigator,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

  LanguageDialogContent(
    selectedLanguage = state.value.input.language,
    onIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Details(data)) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is RecipeInputScreenEffect.OnBottomSheetClosed) navigator.navigateUp()
    }
  }
}