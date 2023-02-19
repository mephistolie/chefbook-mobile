package com.mysty.chefbook.design.components.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape4

@Composable
fun BottomSheetSlider() {
    Box(
        modifier = Modifier
            .padding(top = 6.dp)
            .width(36.dp)
            .height(4.dp)
            .clippedBackground(
                LocalTheme.colors.foregroundPrimary.copy(alpha = 0.2F),
                RoundedCornerShape4
            )
    )
}
