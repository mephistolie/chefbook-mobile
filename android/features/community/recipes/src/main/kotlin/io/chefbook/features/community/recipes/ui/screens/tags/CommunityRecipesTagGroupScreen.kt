package io.chefbook.features.community.recipes.ui.screens.tags

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenEffect
import io.chefbook.features.community.recipes.ui.viewmodel.CommunityRecipesScreenViewModel
import io.chefbook.navigation.navigators.BaseNavigator

@Destination(
  route = "community/recipes/filter/tags",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun CommunityRecipesTagGroupScreen(
  groupId: String?,
  viewModel: CommunityRecipesScreenViewModel,
  navigator: BaseNavigator,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

  state.value.filter.tagGroups.firstOrNull { it.id == groupId }?.let { group ->
    CommunityRecipesTagGroupScreenContent(
      group = group,
      selectedTags = state.value.filter.selectedTags,
      onIntent = viewModel::handleIntent,
    )
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        CommunityRecipesScreenEffect.TagGroupClosed -> navigator.navigateUp()
        else -> Unit
      }
    }
  }
}
