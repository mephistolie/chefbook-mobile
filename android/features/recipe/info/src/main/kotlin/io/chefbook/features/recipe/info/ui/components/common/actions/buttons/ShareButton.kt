package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.design.theme.dimens.IconSize36
import io.chefbook.design.R as designR

@Composable
internal fun ShareButton(
  preview: String?,
  isPreviewLoaded: MutableState<Boolean>,
  onShareClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionsWidgetButton(
    preview = preview,
    isPreviewLoaded = isPreviewLoaded,
    onClick = onShareClick,
    cornerRadius = IconSize36 / 2F,
    horizontalPadding = 0.dp,
    modifier = modifier.size(IconSize36),
    leftIconId = designR.drawable.ic_share,
  )
}
