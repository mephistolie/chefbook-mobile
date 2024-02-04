package io.chefbook.features.recipebook.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenEffect
import io.chefbook.features.recipebook.search.ui.navigation.RecipeBookSearchScreenNavigator
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Destination(route = "recipe_book/search")
@Composable
internal fun RecipeBookSearchScreen(
  navigator: RecipeBookSearchScreenNavigator,
) {
  val viewModel: IRecipeBookSearchScreenViewModel =
    getViewModel<RecipeBookSearchScreenViewModel>()
  val state = viewModel.state.collectAsState()

  val focusManager = LocalFocusManager.current
  val keyboardController = LocalSoftwareKeyboardController.current

  RecipeBookSearchScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeBookSearchScreenEffect.OnCategoryOpened -> {
          focusManager.clearFocus(force = true)
          keyboardController?.hide()
          navigator.openCategoryRecipesScreen(categoryId = effect.categoryId)
        }

        is RecipeBookSearchScreenEffect.OnRecipeOpened -> {
          focusManager.clearFocus(force = true)
          keyboardController?.hide()
          navigator.openRecipeScreen(recipeId = effect.recipeId)
        }

        is RecipeBookSearchScreenEffect.Back -> {
          focusManager.clearFocus(force = true)
          keyboardController?.hide()
          navigator.navigateUp()
        }
      }
    }
  }
}
