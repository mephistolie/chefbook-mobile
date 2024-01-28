package io.chefbook.features.recipebook.dashboard.ui.components.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton

@Composable
internal fun DashboardButton(
  onClick: () -> Unit,
  @DrawableRes iconId: Int,
  tint: Color = LocalTheme.colors.foregroundPrimary,
) {
  DynamicButton(
    onClick = onClick,
    modifier = Modifier.size(44.dp),
    leftIcon = ImageVector.vectorResource(id = iconId),
    unselectedForeground = tint,
    iconsSize = 24.dp,
    debounceInterval = 1000L,
  )
}
