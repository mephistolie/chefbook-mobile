package com.mysty.chefbook.features.recipe.control.ui.components.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.showToast
import com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockEffect
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecipeCategoriesSelectionBlock(
  recipe: RecipeInfo,
  navigator: IBaseNavigator,
) {
  val viewModel: IRecipeCategoriesSelectionBlockViewModel = getViewModel<RecipeCategoriesSelectionBlockViewModel> { parametersOf(recipe) }
  val state = viewModel.state.collectAsState()

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
