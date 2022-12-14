package com.mysty.chefbook.design.components.toolbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.R
import com.mysty.chefbook.design.theme.dimens.DefaultIconSize
import com.mysty.chefbook.design.theme.dimens.ToolbarHeight

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    @DrawableRes
    leftButtonIconId: Int? = R.drawable.ic_arrow_left,
    onLeftButtonClick: () -> Unit = {},
    @DrawableRes
    rightButtonIconId: Int? = null,
    onRightButtonClick: () -> Unit = {},
    onContentClick: () -> Unit = {},
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ToolbarHeight)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leftButtonIconId?.let {
            ToolbarIcon(
                iconId = leftButtonIconId,
                onClick = onLeftButtonClick,
                paddingEnd = 12.dp,
            )
        }
        Column(
            modifier = Modifier
                .padding(
                    start = if (rightButtonIconId != null && leftButtonIconId == null) 36.dp else 0.dp,
                    end = if (leftButtonIconId != null && rightButtonIconId == null) 36.dp else 0.dp,
                )
                .fillMaxWidth()
                .simpleClickable(onClick = onContentClick),
            horizontalAlignment = contentAlignment,
            content = content
        )
        rightButtonIconId?.let {
            ToolbarIcon(
                iconId = rightButtonIconId,
                onClick = onRightButtonClick,
                paddingStart = 12.dp,
            )
        }
    }
}

@Composable
private fun ToolbarIcon(
    @DrawableRes
    iconId: Int,
    onClick: () -> Unit,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
) {
    val colors = LocalTheme.colors

    Icon(
        imageVector = ImageVector.vectorResource(iconId),
        tint = colors.foregroundPrimary,
        modifier = Modifier
            .padding(start = paddingStart, end = paddingEnd)
            .size(DefaultIconSize)
            .simpleClickable(onClick = onClick),
        contentDescription = null,
    )
}
