package io.chefbook.features.recipebook.creation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.recipebook.creation.navigation.RecipeBookCreationScreenNavigator
import io.chefbook.features.recipebook.creation.ui.mvi.RecipeBookCreationScreenEffect
import org.koin.androidx.compose.koinViewModel

@Destination(
  route = "recipe_book/creation",
  style = DestinationStyleBottomSheet::class
)
@Composable
fun RecipeBookCreationScreen(
  navigator: RecipeBookCreationScreenNavigator,
) {
  val viewModel = koinViewModel<RecipeBookCreationScreenViewModel>()

  RecipeControlScreenContent(
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        RecipeBookCreationScreenEffect.RecipeInputScreenOpened -> navigator.openRecipeInputScreen()
        RecipeBookCreationScreenEffect.CategoryInputScreenOpened -> {
          navigator.navigateUp()
          navigator.openCategoryInputScreen()
        }
      }
    }
  }
}
