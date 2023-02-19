package com.mysty.chefbook.features.recipe.info.ui.components.common.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mysty.chefbook.features.recipe.info.R

@Composable
internal fun ShareButton(
  onShareClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionsWidgetButton(
    onClick = onShareClick,
    modifier = modifier,
    leftIconId = R.drawable.ic_share,
  )
}
