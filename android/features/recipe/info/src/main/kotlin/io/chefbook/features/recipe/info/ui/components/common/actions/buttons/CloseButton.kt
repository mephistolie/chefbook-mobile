package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.design.theme.dimens.IconSize36
import io.chefbook.design.R as designR

@Composable
internal fun CloseButton(
  preview: String?,
  isPreviewLoaded: MutableState<Boolean>,
  onCloseClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionsWidgetButton(
    preview = preview,
    isPreviewLoaded = isPreviewLoaded,
    onClick = onCloseClick,
    cornerRadius = IconSize36 / 2F,
    modifier = modifier.size(IconSize36),
    horizontalPadding = 0.dp,
    leftIconId = designR.drawable.ic_cross,
  )
}
