package com.mysty.chefbook.features.recipebook.dashboard.ui.components.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape16
import com.mysty.chefbook.features.recipebook.dashboard.R

@Composable
internal  fun NewCategoryCard(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTheme.colors

    val pressed = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 8.dp)
            .aspectRatio(0.8f)
            .scalingClickable(pressed) { onClicked() }
            .clippedBackground(colors.backgroundSecondary, RoundedCornerShape16),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null,
            tint = colors.foregroundPrimary,
            modifier = Modifier.size(32.dp)
        )
        Shading(pressed.value)
    }
}
