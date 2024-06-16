package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.chefbook.design.theme.dimens.ComponentHeight36
import io.chefbook.design.theme.dimens.ComponentHeight40
import io.chefbook.design.theme.dimens.IconSize36
import io.chefbook.libs.models.language.Language
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.design.R as designR

@Composable
internal fun LanguageButton(
  language: Language,
  preview: String?,
  isPreviewLoaded: MutableState<Boolean>,
  onLanguageClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val resources = LocalContext.current.resources

  ActionsWidgetButton(
    text = "${language.flag} ${language.localizedName(resources)}",
    preview = preview,
    isPreviewLoaded = isPreviewLoaded,
    onClick = onLanguageClick,
    modifier = modifier.height(ComponentHeight36),
    rightIconId = designR.drawable.ic_arrow_down,
    rightIconModifier = Modifier.padding(top = 2.dp),
  )
}
