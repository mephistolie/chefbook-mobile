package com.cactusknights.chefbook.ui.views.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.simpleClickable
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    leftButtonVisible: Boolean = true,
    leftButtonIcon: ImageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
    onLeftButtonClick: () -> Unit = {},
    rightButtonVisible: Boolean = false,
    rightButtonIcon: ImageVector = ImageVector.vectorResource(R.drawable.ic_check),
    onRightButtonClick: () -> Unit = {},
    onContentClick: () -> Unit = {},
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = ChefBookTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leftButtonVisible) {
            Icon(
                imageVector = leftButtonIcon,
                tint = colors.foregroundPrimary,
                modifier = Modifier
                    .size(36.dp)
                    .simpleClickable(onClick = onLeftButtonClick)
                    .padding(end = 12.dp)
                    .aspectRatio(1F),
                contentDescription = null,
            )
        }
        Column(
            modifier = Modifier
                .padding(
                    start = if (rightButtonVisible && !leftButtonVisible) 36.dp else 0.dp,
                    end = if (leftButtonVisible && !rightButtonVisible) 36.dp else 0.dp,
                )
                .fillMaxWidth()
                .simpleClickable(onClick = onContentClick),
            horizontalAlignment = contentAlignment,
            content = content
        )
        if (rightButtonVisible) {
            Icon(
                imageVector = rightButtonIcon,
                tint = colors.foregroundPrimary,
                modifier = Modifier
                    .size(36.dp)
                    .simpleClickable(onClick = onRightButtonClick)
                    .padding(start = 12.dp)
                    .aspectRatio(1F),
                contentDescription = null,
            )
        }
    }
}