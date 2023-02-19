package com.mysty.chefbook.features.home.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton

@Composable
internal fun HomeScreenTabButton(
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    isSelected: Boolean,
) {
    val colors = LocalTheme.colors

    DynamicButton(
        onClick = onClick,
        modifier = Modifier.size(44.dp),
        leftIcon = ImageVector.vectorResource(id = iconId),
        isSelected = isSelected,
        selectedForeground = colors.foregroundPrimary,
        selectedBackground = colors.backgroundTertiary,
        unselectedForeground = colors.foregroundPrimary,
        unselectedBackground = colors.backgroundSecondary,
        iconsSize = 24.dp,
        debounceInterval = 1000L,
    )
}
