package io.chefbook.features.recipe.input.ui.screens.ingredients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputIngredientsScreenIntent
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.screens.ingredients.components.AddIngredientItemBlock
import io.chefbook.features.recipe.input.ui.screens.ingredients.components.ingredientsList
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.core.android.R as coreR
import io.chefbook.design.components.toolbar.Toolbar
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
internal fun RecipeInputIngredientScreenContent(
  state: RecipeInput,
  onIntent: (RecipeInputScreenIntent) -> Unit,
  onIngredientsIntent: (RecipeInputIngredientsScreenIntent) -> Unit,
) {
  val haptic = LocalHapticFeedback.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val reorderableState = rememberReorderableLazyListState({ from, to ->
    onIngredientsIntent(RecipeInputIngredientsScreenIntent.MoveIngredientItem(from.index, to.index))
  })

  Column(
    modifier = Modifier
      .statusBarsPadding()
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Toolbar(
      onLeftButtonClick = { onIntent(RecipeInputScreenIntent.Back) },
      modifier = Modifier.padding(horizontal = 12.dp)
    ) {
      Text(
        text = stringResource(coreR.string.common_general_ingredients),
        maxLines = 1,
        style = typography.h4,
        color = colors.foregroundPrimary,
      )
    }
    LazyColumn(
      state = reorderableState.listState,
      modifier = Modifier
        .weight(1F)
        .fillMaxHeight()
        .reorderable(reorderableState)
        .detectReorderAfterLongPress(reorderableState),
      horizontalAlignment = Alignment.Start,
    ) {
      ingredientsList(
        ingredients = state.ingredients,
        reorderableState = reorderableState,
        haptic = haptic,
        onIntent = onIngredientsIntent,
      )
      item {
        AddIngredientItemBlock(
          onIntent = onIngredientsIntent,
          modifier = Modifier
            .fillMaxWidth()
            .padding(
              start = 12.dp,
              top = 16.dp,
              end = 12.dp,
              bottom = 32.dp
            ),
        )
      }
    }
    val isContinueAvailable = isContinueAvailable(state.ingredients)
    DynamicButton(
      text = stringResource(coreR.string.common_general_continue),
      isSelected = isContinueAvailable,
      isEnabled = isContinueAvailable,
      modifier = Modifier
        .navigationBarsPadding()
        .padding(
          start = 12.dp,
          top = 8.dp,
          end = 12.dp,
          bottom = 4.dp,
        )
        .fillMaxWidth()
        .height(56.dp),
      onClick = { onIntent(RecipeInputScreenIntent.Continue) },
    )
  }
}

private fun isContinueAvailable(ingredients: List<IngredientsItem>) =
  ingredients.any { it is IngredientsItem.Ingredient && it.name.isNotBlank() }