package com.cactusknights.chefbook.ui.views.checkboxes

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.simpleClickable
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun CircleCheckbox(
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    size: Dp = 24.dp,
    color: Color = ChefBookTheme.colors.tintPrimary,
    iconPadding: Dp = 0.dp,
    enabled: Boolean = true,
) {
    val colors = ChefBookTheme.colors

    val transition = updateTransition(isChecked, label = "isSelected")

    val borderWidth by transition.animateDp(label = "borderWidth") { selected ->
        if (selected) (size.value / 2).dp else (size.value / 12).dp
    }

    val alpha by transition.animateFloat(label = "alpha") { selected ->
        if (selected) 1F else 0F
    }

    val baseModifier =
        if (enabled) {
            Modifier
                .size(size)
                .simpleClickable(onCheckedChange)
                .clip(CircleShape)
        } else {
            Modifier
                .size(size)
                .clip(CircleShape)
        }


    val checkboxModifier =
        Modifier
            .fillMaxSize()
            .border(
                BorderStroke(
                    width = borderWidth,
                    color = color,
                ),
                shape = CircleShape,
            )

    Box(
        modifier = baseModifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = checkboxModifier
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_check),
            tint = colors.backgroundPrimary,
            contentDescription = null,
            modifier = Modifier
                .padding(iconPadding)
                .alpha(alpha)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightCircleCheckbox() {
    ThemedCircleCheckbox(false,false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkCircleCheckbox() {
    ThemedCircleCheckbox(true, true)
}

@Composable
private fun ThemedCircleCheckbox(
    checked: Boolean,
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            CircleCheckbox(
                isChecked = checked,
                onCheckedChange = {},
                size = 24.dp
            )
        }
    }
}