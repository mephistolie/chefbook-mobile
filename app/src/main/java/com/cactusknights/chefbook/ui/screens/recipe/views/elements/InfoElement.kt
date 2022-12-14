package com.cactusknights.chefbook.ui.screens.recipe.views.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

@Composable
fun InfoElement(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

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