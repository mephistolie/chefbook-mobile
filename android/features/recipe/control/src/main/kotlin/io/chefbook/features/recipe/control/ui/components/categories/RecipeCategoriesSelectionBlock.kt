package io.chefbook.features.recipe.control.ui.components.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockEffect
import io.chefbook.core.android.showToast
import io.chefbook.navigation.navigators.BaseNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecipeCategoriesSelectionBlock(
  recipe: DecryptedRecipeInfo,
  navigator: BaseNavigator,
) {
  val viewModel: IRecipeCategoriesSelectionBlockViewModel = getViewModel<RecipeCategoriesSelectionBlockViewModel> { parametersOf(recipe) }
  val state = viewModel.state.collectAsState()

  val context = LocalContext.current

  RecipeCategoriesSelectionBlockContent(
    state = state.value,
    onIntent = viewModel::handleIntent
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeCategoriesSelectionBlockEffect.ShowToast -> context.showToast(effect.messageId)
        is RecipeCategoriesSelectionBlockEffect.Close -> navigator.navigateUp()
      }
    }
  }
}
