package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.design.R as designR

@Composable
internal fun InfoButton(
  preview: String?,
  isPreviewLoaded: MutableState<Boolean>,
  onInfoClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionsWidgetButton(
    preview = preview,
    isPreviewLoaded = isPreviewLoaded,
    onClick = onInfoClick,
    modifier = modifier.width(50.dp),
    leftIconId = designR.drawable.ic_info,
  )
}
