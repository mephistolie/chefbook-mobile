package io.chefbook.features.recipe.control.ui.components.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import io.chefbook.core.android.showToast
import io.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockEffect
import io.chefbook.navigation.navigators.BaseNavigator
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecipeCategoriesSelectionBlock(
  recipe: DecryptedRecipeInfo,
  navigator: BaseNavigator,
) {
  val viewModel = koinViewModel<RecipeCategoriesSelectionBlockViewModel> { parametersOf(recipe) }
  val state = viewModel.state.collectAsStateWithLifecycle()

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
