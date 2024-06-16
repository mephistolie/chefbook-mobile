package io.chefbook.features.recipe.info.ui.components.common.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.design.components.spacers.HorizontalSpacer
import io.chefbook.design.theme.dimens.ComponentHeight48
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.InfoButton
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.RateButton
import io.chefbook.features.recipe.info.ui.components.common.actions.buttons.ManagementButton
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe

@Composable
internal fun ActionsWidget(
  recipe: Recipe,
  isPreviewLoaded: MutableState<Boolean>,
  modifier: Modifier = Modifier,
  onRateClick: () -> Unit,
  onSaveClick: () -> Unit,
  onInfoClick: () -> Unit,
) {
  Row(modifier = modifier) {
    ManagementButton(
      recipe = recipe,
      isPreviewLoaded = isPreviewLoaded,
      modifier = Modifier
        .weight(1F)
        .fillMaxWidth()
        .height(ComponentHeight48),
      onSaveClick = onSaveClick,
    )
    HorizontalSpacer(8.dp)
    RateButton(
      preview = recipe.preview,
      isPreviewLoaded = isPreviewLoaded,
      rating = recipe.rating,
      modifier = Modifier
        .wrapContentWidth()
        .height(ComponentHeight48)
        .widthIn(min = ComponentHeight48),
      onRateClick = onRateClick,
    )
    HorizontalSpacer(8.dp)

    InfoButton(
      preview = recipe.preview,
      isPreviewLoaded = isPreviewLoaded,
      modifier = Modifier
        .wrapContentWidth()
        .height(ComponentHeight48),
      onInfoClick = onInfoClick,
    )
  }
}
