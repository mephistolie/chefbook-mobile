package com.cactusknights.chefbook.ui.views.dialogs.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.modifiers.clippedBackground

@Composable
fun BottomSheetSlider() {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .width(48.dp)
            .height(6.dp)
            .clippedBackground(
                ChefBookTheme.colors.foregroundPrimary.copy(alpha = 0.2F),
                RoundedCornerShape(4.dp)
            )
    )
}
