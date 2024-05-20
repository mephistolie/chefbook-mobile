package io.chefbook.features.recipebook.favourite.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenEffect
import io.chefbook.features.recipebook.favourite.ui.navigation.RecipeBookFavouriteScreenNavigator
import org.koin.androidx.compose.koinViewModel

@Destination(route = "recipe_book/favourite")
@Composable
internal fun FavouriteRecipesScreen(
  navigator: RecipeBookFavouriteScreenNavigator,
) {
  val viewModel = koinViewModel<FavouriteRecipesScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  FavouriteRecipesScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is FavouriteRecipesScreenEffect.OnRecipeOpened -> navigator.openRecipeScreen(recipeId = effect.recipeId)
        is FavouriteRecipesScreenEffect.Back -> navigator.navigateUp()
      }
    }
  }
}
