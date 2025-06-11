package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.design.theme.dimens.ComponentHeight36
import io.chefbook.ui.common.components.profile.ProfileAvatar

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
