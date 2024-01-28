package io.chefbook.features.recipe.control.ui.components.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockIntent
import io.chefbook.features.recipe.control.ui.components.categories.mvi.RecipeCategoriesSelectionBlockState
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.components.toolbar.Toolbar
import io.chefbook.design.R as designR
import io.chefbook.features.recipe.control.R

@OptIn(ExperimentalLayoutApi::class)
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
      rightButtonIconId = designR.drawable.ic_check,
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
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      for (category in state.categories) {
        DynamicButton(
          text = "${category.emoji.orEmpty()} ${category.name}".trim(),
          onClick = {
            onIntent(RecipeCategoriesSelectionBlockIntent.ChangeSelectStatus(category.id))
          },
          modifier = Modifier.height(38.dp),
          cornerRadius = 12.dp,
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
