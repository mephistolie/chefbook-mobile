package io.chefbook.features.recipe.input.ui.dialogs.encryption

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.navigation.navigators.BaseNavigator

@Destination(
  route = "encryption",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun EncryptionStateDialog(
  viewModel: IRecipeInputScreenViewModel,
  navigator: BaseNavigator,
) {
  val state = viewModel.state.collectAsState()

  EncryptionStateDialogContent(
    isEncrypted = state.value.input.hasEncryption,
    onIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Details(data)) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is RecipeInputScreenEffect.OnBottomSheetClosed) navigator.navigateUp()
    }
  }
}