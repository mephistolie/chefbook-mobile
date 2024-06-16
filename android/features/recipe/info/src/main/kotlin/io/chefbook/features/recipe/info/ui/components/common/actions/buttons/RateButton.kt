package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.design.R as designR

@Composable
internal fun RateButton(
  rating: RecipeMeta.Rating,
  preview: String?,
  isPreviewLoaded: MutableState<Boolean>,
  onRateClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val score = rating.score

  ActionsWidgetButton(
    preview = preview,
    isPreviewLoaded = isPreviewLoaded,
    onClick = onRateClick,
    modifier = modifier,
    leftIconId = designR.drawable.ic_star,
    text = if (score != null) "$score" else null,
    isSelected = score != null && score > 0,
  )
}
