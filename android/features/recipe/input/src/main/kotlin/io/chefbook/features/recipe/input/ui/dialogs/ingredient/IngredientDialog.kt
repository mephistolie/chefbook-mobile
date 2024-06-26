package io.chefbook.features.recipe.input.ui.dialogs.ingredient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import io.chefbook.navigation.navigators.BaseNavigator
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem

@Destination(
  route = "ingredient",
  style = DestinationStyleBottomSheet::class,
)
@Composable
internal fun IngredientDialog(
  ingredientId: String,
  viewModel: RecipeInputScreenViewModel,
  navigator: BaseNavigator,
) {
  val state = viewModel.state.collectAsStateWithLifecycle()

  val ingredient = state.value.input.ingredients.find { it.id == ingredientId } as IngredientsItem.Ingredient

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
