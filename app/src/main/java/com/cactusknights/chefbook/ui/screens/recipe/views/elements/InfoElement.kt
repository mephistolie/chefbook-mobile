package com.cactusknights.chefbook.ui.screens.recipe.views.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun InfoElement(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = name,
            style = typography.subhead1,
            color = colors.foregroundSecondary,
        )
        Text(
            text = value,
            style = typography.headline1,
            color = colors.foregroundPrimary,
        )
    }
}