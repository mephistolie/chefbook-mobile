package io.chefbook.features.community.recipes.ui.screens.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenEffect
import io.chefbook.features.community.recipes.ui.viewmodel.CommunityRecipesScreenViewModel
import io.chefbook.navigation.navigators.BaseNavigator

@Destination(
  route = "community/recipes/filter",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun CommunityRecipesFilterScreen(
  focusSearch: Boolean = false,
  viewModel: CommunityRecipesScreenViewModel,
  navigator: BaseNavigator,
) {
  val state = viewModel.state.collectAsState()

  CommunityRecipesFilterScreenContent(
    state = state.value.filter,
    onIntent = viewModel::handleIntent,
    focusSearch = focusSearch,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        CommunityRecipesScreenEffect.FilterClosed -> navigator.navigateUp()
        else -> Unit
      }
    }
  }
}
