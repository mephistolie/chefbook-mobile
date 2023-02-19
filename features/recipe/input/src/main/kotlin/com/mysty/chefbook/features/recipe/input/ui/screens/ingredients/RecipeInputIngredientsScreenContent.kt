package com.mysty.chefbook.features.recipe.input.ui.screens.ingredients

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
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.toolbar.Toolbar
import com.mysty.chefbook.features.recipe.input.R
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputIngredientsScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.screens.ingredients.components.AddIngredientItemBlock
import com.mysty.chefbook.features.recipe.input.ui.screens.ingredients.components.ingredientsList
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
        text = stringResource(R.string.common_general_ingredients),
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
      text = stringResource(R.string.common_general_continue),
      isSelected = isContinueAvailable,
      isEnabled = isContinueAvailable,
      cornerRadius = 16.dp,
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

private fun isContinueAvailable(ingredients: List<IngredientItem>) =
  ingredients.any { it is IngredientItem.Ingredient && it.name.isNotBlank() }