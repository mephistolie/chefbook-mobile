package com.mysty.chefbook.features.recipe.info.ui.components.common.actions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.features.recipe.info.R

@Composable
internal fun LikeButton(
  recipe: Recipe,
  onLikeClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val likes = recipe.likes ?: 0

  ActionsWidgetButton(
    onClick = onLikeClick,
    modifier = modifier,
    leftIconId = R.drawable.ic_like,
    text = if (likes > 0) recipe.likes.toString() else null,
    isSelected = recipe.isLiked,
  )
}
