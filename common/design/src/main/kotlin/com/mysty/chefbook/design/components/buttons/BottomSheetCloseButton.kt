package com.mysty.chefbook.design.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.R
import com.mysty.chefbook.design.theme.dimens.MediumIconSize

@Composable
fun BottomSheetCloseButton(
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = horizontalPadding,
    iconSize: Dp = MediumIconSize,
    onClick: () -> Unit,
) {
    val colors = LocalTheme.colors

    CircleIconButton(
        iconId = R.drawable.ic_cross,
        onClick = onClick,
        modifier = Modifier
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding,
            )
            .size(iconSize),
        colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
        tint = Color.White,
    )
}
