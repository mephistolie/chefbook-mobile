package com.mysty.chefbook.features.recipe.input.ui.dialogs.saved

import androidx.compose.runtime.Composable
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.mysty.chefbook.navigation.styles.NonDismissibleDialog
import com.ramcosta.composedestinations.annotation.Destination

@Destination(
  route = "saved",
  style = NonDismissibleDialog::class,
)
@Composable
internal fun RecipeSavedDialog(
  viewModel: IRecipeInputScreenViewModel,
) {
  RecipeSavedDialogContent(
    onOpenRecipe = {
      viewModel.handleIntent(RecipeInputScreenIntent.Close(openRecipeScreen = true))
    },
    onCloseInput = { viewModel.handleIntent(RecipeInputScreenIntent.Close(openRecipeScreen = true)) },
  )
}
