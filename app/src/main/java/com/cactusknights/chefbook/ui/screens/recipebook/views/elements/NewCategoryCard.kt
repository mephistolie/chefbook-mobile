package com.cactusknights.chefbook.ui.screens.recipebook.views.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.scalingClickable

@Composable
fun NewCategoryCard(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors

    val pressed = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 8.dp)
            .aspectRatio(0.8f)
            .scalingClickable(pressed) { onClicked() }
            .clip(RoundedCornerShape(16.dp))
            .background(colors.backgroundSecondary),
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
