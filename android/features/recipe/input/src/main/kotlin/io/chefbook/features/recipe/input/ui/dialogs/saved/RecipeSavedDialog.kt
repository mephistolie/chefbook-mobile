package io.chefbook.features.recipe.input.ui.dialogs.saved

import androidx.compose.runtime.Composable
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.navigation.styles.NonDismissibleDialog

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
