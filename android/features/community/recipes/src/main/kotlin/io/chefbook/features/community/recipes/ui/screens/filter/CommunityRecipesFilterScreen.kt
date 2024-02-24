package io.chefbook.features.community.recipes.ui.screens.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.community.recipes.navigation.CommunityRecipesFilterScreenNavigator
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenEffect
import io.chefbook.features.community.recipes.ui.viewmodel.CommunityRecipesScreenViewModel

@Destination(
  route = "community/recipes/filter",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun CommunityRecipesFilterScreen(
  focusSearch: Boolean = false,
  scrollToTags: Boolean = false,
  viewModel: CommunityRecipesScreenViewModel,
  navigator: CommunityRecipesFilterScreenNavigator,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

  CommunityRecipesFilterScreenContent(
    state = state.value.filter,
    onIntent = viewModel::handleIntent,
    focusSearch = focusSearch,
    scrollToTags = scrollToTags,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is CommunityRecipesScreenEffect.FilterClosed -> navigator.navigateUp()
        is CommunityRecipesScreenEffect.TagGroupOpened -> navigator.openTagGroupScreen(effect.groupId)
        else -> Unit
      }
    }
  }
}
