package com.cactusknights.chefbook.ui.screens.recipe.views.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

@Composable
fun DietElement(
    name: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = value.toString(),
            style = typography.h3,
            color = colors.foregroundPrimary,
        )
        Text(
            text = name,
            style = typography.headline2,
            color = colors.foregroundSecondary,
        )
    }
}