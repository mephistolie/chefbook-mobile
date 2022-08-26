package com.cactusknights.chefbook.ui.views.buttons

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun IconButton(
    image: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = ChefBookTheme.colors.foregroundPrimary,
) {
    Button(
        onClick = onClick,
        elevation = null,
        modifier = modifier.aspectRatio(1F)
    ) {
        Icon(
            imageVector = image,
            tint = tint,
            contentDescription = null,
        )
    }
}
