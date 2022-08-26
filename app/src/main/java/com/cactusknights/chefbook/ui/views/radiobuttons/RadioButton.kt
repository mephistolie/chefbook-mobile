package com.cactusknights.chefbook.ui.views.radiobuttons

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.core.ui.simpleClickable
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun RadioButton(
    isSelected: Boolean,
    onSelected: () -> Unit,
    size: Dp = 24.dp,
    color: Color = ChefBookTheme.colors.tintPrimary,
    enabled: Boolean = true,
) {

    val transition = updateTransition(isSelected, label = "isSelected")

    val borderWidth by transition.animateDp(label = "borderWidth") { selected ->
        if (selected) (size.value / 3).dp else (size.value / 12).dp
    }

    val baseModifier =
        if (enabled) {
            Modifier
                .size(size)
                .simpleClickable(onSelected)
        } else {
            Modifier.size(size)
        }

    Box(
        modifier = baseModifier
            .clip(CircleShape)
            .border(
                BorderStroke(
                    width = borderWidth,
                    color = color,
                ),
                shape = CircleShape,
            )
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightRadioButton() {
    ThemedCircleCheckbox(false,false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkRadioButton() {
    ThemedCircleCheckbox(true, true)
}

@Composable
private fun ThemedCircleCheckbox(
    selected: Boolean,
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            RadioButton(
                isSelected = selected,
                onSelected = {},
                size = 48.dp
            )
        }
    }
}