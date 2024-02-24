package io.chefbook.features.community.recipes.ui.screens.content

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenEffect
import io.chefbook.features.community.recipes.navigation.CommunityRecipesScreenNavigator
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenIntent
import io.chefbook.features.community.recipes.ui.viewmodel.CommunityRecipesScreenViewModel
import io.chefbook.libs.logger.Logger

@Destination("community/recipes")
@Composable
fun CommunityRecipesContentScreen(
  viewModel: CommunityRecipesScreenViewModel,
  navigator: CommunityRecipesScreenNavigator,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

  CommunityRecipesScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  BackHandler { viewModel.handleIntent(CommunityRecipesScreenIntent.Back) }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is CommunityRecipesScreenEffect.Back -> navigator.navigateUp()
        is CommunityRecipesScreenEffect.ProfileScreenOpened -> navigator.openProfileScreen()
        is CommunityRecipesScreenEffect.LanguagesPickerOpened -> navigator.openCommunityLanguagesPickerScreen()

        is CommunityRecipesScreenEffect.FilterOpened -> navigator.openCommunityRecipesFilterScreen(
          focusSearch = effect.focusSearch,
          scrollToTags = effect.scrollToTags,
        )

        is CommunityRecipesScreenEffect.RecipeScreenOpened -> navigator.openRecipeScreen(effect.recipeId)
        is CommunityRecipesScreenEffect.RecipeInputScreenOpened -> navigator.openRecipeInputScreen()
        is CommunityRecipesScreenEffect.FilterClosed -> Unit
        is CommunityRecipesScreenEffect.TagGroupClosed -> Unit
        is CommunityRecipesScreenEffect.TagGroupOpened -> Unit
      }
    }
  }
}
