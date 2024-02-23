package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.R as coreR
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.design.R as designR

@Composable
internal fun RateButton(
  rating: RecipeMeta.Rating,
  onRateClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val score = rating.score

  ActionsWidgetButton(
    onClick = onRateClick,
    modifier = modifier.widthIn(min = 50.dp),
    leftIconId = designR.drawable.ic_star,
    text = if (score != null) "$score" else null,
    isSelected = score != null && score > 0,
  )
}
