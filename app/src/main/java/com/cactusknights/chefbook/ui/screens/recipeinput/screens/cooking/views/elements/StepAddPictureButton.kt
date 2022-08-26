package com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking.views.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.simpleClickable
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun StepAddPictureButton(
    onAddPictureClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors

    Box(
        modifier = modifier
            .width(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.backgroundSecondary)
            .aspectRatio(1.5F)
            .simpleClickable(onAddPictureClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_image),
            tint = colors.foregroundPrimary,
            modifier = Modifier.size(24.dp),
            contentDescription = null,
        )
    }
}