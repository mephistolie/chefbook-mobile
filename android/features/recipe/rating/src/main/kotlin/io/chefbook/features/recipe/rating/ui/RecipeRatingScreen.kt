package io.chefbook.features.recipe.rating.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.recipe.rating.ui.mvi.RecipeRatingScreenEffect
import io.chefbook.navigation.navigators.BaseNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination(
  route = "recipe/rating",
  style = DestinationStyleBottomSheet::class
)
@Composable
fun RecipeRatingScreen(
  recipeId: String,
  navigator: BaseNavigator,
) {
  val viewModel = koinViewModel<RecipeRatingScreenViewModel> { parametersOf(recipeId) }
  val state = viewModel.state.collectAsStateWithLifecycle()

  RecipeRatingScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is RecipeRatingScreenEffect.Close -> navigator.navigateUp()
      }
    }
  }
}