package com.mysty.chefbook.features.recipe.input.ui.screens.cooking.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.features.recipe.input.R
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputCookingScreenIntent

@Composable
internal fun AddCookingItemBlock(
  onIntent: (RecipeInputCookingScreenIntent) -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = LocalTheme.colors

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    DynamicButton(
      leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
      text = stringResource(R.string.common_general_section),
      unselectedForeground = colors.foregroundPrimary,
      modifier = Modifier
        .height(36.dp),
      onClick = { onIntent(RecipeInputCookingScreenIntent.AddCookingSection) }
    )
    DynamicButton(
      leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
      text = stringResource(R.string.common_general_step),
      unselectedForeground = colors.foregroundPrimary,
      modifier = Modifier
        .height(36.dp),
      onClick = { onIntent(RecipeInputCookingScreenIntent.AddStep) }
    )
  }
}