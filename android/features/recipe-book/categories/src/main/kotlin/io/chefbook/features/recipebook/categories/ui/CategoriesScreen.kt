package io.chefbook.features.recipebook.categories.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.recipebook.categories.ui.mvi.CategoriesScreenEffect
import io.chefbook.features.recipebook.categories.ui.navigation.CategoriesScreenNavigator
import org.koin.androidx.compose.koinViewModel

@Destination(route = "recipe_book/categories")
@Composable
internal fun CategoriesScreen(
  navigator: CategoriesScreenNavigator,
) {
  val viewModel = koinViewModel<CategoriesScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  CategoriesScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is CategoriesScreenEffect.CategoryOpened -> navigator.openCategoryRecipesScreen(effect.categoryId)
        is CategoriesScreenEffect.TagOpened -> navigator.openTagRecipesScreen(effect.tagId)
        is CategoriesScreenEffect.Back -> navigator.navigateUp()
      }
    }
  }
}
