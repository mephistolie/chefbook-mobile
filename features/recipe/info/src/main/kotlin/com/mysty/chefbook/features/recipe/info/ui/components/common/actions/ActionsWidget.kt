package com.mysty.chefbook.features.recipe.info.ui.components.common.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.recipe.domain.entities.Recipe

@Composable
internal fun ActionsWidget(
  recipe: Recipe,
  onLikeClick: () -> Unit,
  onSaveClick: () -> Unit,
  onShareClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(modifier = modifier) {
    ManagementButton(
      recipe = recipe,
      onSaveClick = onSaveClick,
      modifier = Modifier
        .weight(1F)
        .fillMaxWidth(),
    )
    LikeButton(
      recipe = recipe,
      onLikeClick = onLikeClick,
      modifier = Modifier
        .padding(start = 8.dp)
        .wrapContentWidth(),
    )
    ShareButton(
      onShareClick = onShareClick,
      modifier = Modifier
        .padding(start = 8.dp)
        .wrapContentWidth(),
    )
  }
}
