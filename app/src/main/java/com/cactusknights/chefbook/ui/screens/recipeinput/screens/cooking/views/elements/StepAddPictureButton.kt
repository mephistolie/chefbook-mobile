package com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking.views.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.themes.Shapes.RoundedCornerShape12
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable

@Composable
fun StepAddPictureButton(
    onAddPictureClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors

    Box(
        modifier = modifier
            .width(64.dp)
            .clippedBackground(colors.backgroundSecondary, RoundedCornerShape12)
            .aspectRatio(1.5F)
            .simpleClickable(onClick = onAddPictureClick),
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
