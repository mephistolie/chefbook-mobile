package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.design.R as designR

@Composable
internal fun LikeButton(
  recipe: Recipe,
  onLikeClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val score = recipe.rating.score
  val likes = recipe.rating.votes

  ActionsWidgetButton(
    onClick = onLikeClick,
    modifier = modifier.widthIn(min = 50.dp),
    leftIconId = designR.drawable.ic_like,
    text = if (likes > 0) likes.toString() else null,
    isSelected = score != null && score > 0,
  )
}
