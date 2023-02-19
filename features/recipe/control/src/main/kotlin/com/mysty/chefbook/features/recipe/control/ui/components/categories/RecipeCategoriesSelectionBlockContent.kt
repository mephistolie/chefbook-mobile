package com.mysty.chefbook.features.recipe.control.ui.components.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.toolbar.Toolbar
import com.mysty.chefbook.features.recipe.control.R
import com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockIntent
import com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockState

@Composable
internal fun RecipeCategoriesSelectionBlockContent(
  state: RecipeCategoriesSelectionBlockState,
  onIntent: (RecipeCategoriesSelectionBlockIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column {
    Toolbar(
      onLeftButtonClick = { onIntent(RecipeCategoriesSelectionBlockIntent.Cancel) },
      rightButtonIconId = R.drawable.ic_check,
      rightButtonTint = colors.tintPrimary,
      rightIconEndPadding = 2.dp,
      onRightButtonClick = { onIntent(RecipeCategoriesSelectionBlockIntent.ConfirmSelection) }
    ) {
      Text(
        text = stringResource(R.string.common_recipe_control_screen_choose_categories),
        style = typography.h4,
        color = colors.foregroundPrimary,
        maxLines = 1,
      )
    }
    FlowRow(
      modifier = Modifier
        .wrapContentWidth()
        .padding(top = 12.dp, bottom = 196.dp),
      mainAxisSpacing = 8.dp,
      crossAxisSpacing = 8.dp,
    ) {
      for (category in state.categories) {
        DynamicButton(
          text = "${category.cover.orEmpty()} ${category.name}".trim(),
          onClick = {
            onIntent(RecipeCategoriesSelectionBlockIntent.ChangeSelectStatus(category.id))
          },
          modifier = Modifier.height(38.dp),
          horizontalPadding = 8.dp,
          unselectedForeground = colors.foregroundPrimary,
          selectedBackground = colors.foregroundPrimary,
          unselectedBackground = colors.backgroundTertiary,
          isSelected = category.id in state.selectedCategories,
        )
      }
    }
  }
}
