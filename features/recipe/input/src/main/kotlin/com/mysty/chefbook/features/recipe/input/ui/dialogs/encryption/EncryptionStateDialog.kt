package com.mysty.chefbook.features.recipe.input.ui.dialogs.encryption

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
  route = "encryption",
  style = DestinationStyle.BottomSheet::class,
)
@Composable
internal fun EncryptionStateDialog(
  viewModel: IRecipeInputScreenViewModel,
  navigator: IBaseNavigator,
) {
  val state = viewModel.state.collectAsState()

  EncryptionStateDialogContent(
    isEncrypted = state.value.input.isEncrypted,
    onIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Details(data)) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is RecipeInputScreenEffect.OnBottomSheetClosed) navigator.navigateUp()
    }
  }
}