package com.cactusknights.chefbook.ui.screens.recipe.views.elements

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun Section(
    name: String,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Text(
        text = name,
        style = typography.h3,
        color = colors.foregroundSecondary,
        modifier = modifier,
    )
}
