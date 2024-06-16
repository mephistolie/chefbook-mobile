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
import io.chefbook.ui.common.components.profile.ProfileAvatar
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.design.R as designR

@Composable
internal fun ProfileButton(
  name: String,
  avatar: String?,
  preview: String?,
  isPreviewLoaded: MutableState<Boolean>,
  onLanguageClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActionsWidgetButton(
    text = name,
    preview = preview,
    isPreviewLoaded = isPreviewLoaded,
    onClick = onLanguageClick,
    horizontalPadding = 8.dp,
    modifier = modifier.height(ComponentHeight36),
    leftContent = {
      ProfileAvatar(
        url = avatar,
        size = 20.dp,
      )
    },
  )
}
