package com.cactusknights.chefbook.ui.screens.recipe.views.elements

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

@Composable
fun Section(
    name: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Text(
        text = name,
        style = typography.h3,
        color = colors.foregroundSecondary,
        modifier = modifier,
    )
}
