package io.chefbook.features.recipe.info.ui.components.common.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.LikeButton
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.ManagementButton
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.ShareButton
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe

@Composable
internal fun ActionsWidget(
  recipe: Recipe,
  modifier: Modifier = Modifier,
  onLikeClick: () -> Unit,
  onSaveClick: () -> Unit,
  onShareClick: () -> Unit,
) {
  Row(modifier = modifier) {
    ManagementButton(
      recipe = recipe,
      modifier = Modifier
        .weight(1F)
        .fillMaxWidth(),
      onSaveClick = onSaveClick,
    )
    LikeButton(
      recipe = recipe,
      modifier = Modifier
        .padding(start = 8.dp)
        .wrapContentWidth(),
      onLikeClick = onLikeClick,
    )
    ShareButton(
      modifier = Modifier
        .padding(start = 8.dp)
        .wrapContentWidth(),
      onShareClick = onShareClick,
    )
  }
}
