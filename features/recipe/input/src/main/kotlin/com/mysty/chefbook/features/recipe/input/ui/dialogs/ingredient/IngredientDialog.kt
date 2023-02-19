package com.mysty.chefbook.features.recipe.input.ui.dialogs.ingredient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.viewmodel.IRecipeInputScreenViewModel
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle

@Destination(
  route = "ingredient",
  style = DestinationStyle.BottomSheet::class,
)
@Composable
internal fun IngredientDialog(
  ingredientId: String,
  viewModel: IRecipeInputScreenViewModel,
  navigator: IBaseNavigator,
) {
  val state = viewModel.state.collectAsState()

  val ingredient =
      state.value.input.ingredients.find { it.id == ingredientId } as IngredientItem.Ingredient

  IngredientDialogContent(
    state = ingredient,
    onIntent = viewModel::handleIntent,
    onIngredientsIntent = { data -> viewModel.handleIntent(RecipeInputScreenIntent.Ingredients(data)) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      if (effect is RecipeInputScreenEffect.OnBottomSheetClosed) navigator.navigateUp()
    }
  }
}
