package com.cactusknights.chefbook.ui.views.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable

@Composable
fun DynamicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 10.dp,
    cornerRadius: Dp = 12.dp,
    selectedBackground: Color = ChefBookTheme.colors.tintPrimary,
    unselectedBackground: Color = ChefBookTheme.colors.backgroundSecondary,
    text: String? = null,
    textStyle: TextStyle = ChefBookTheme.typography.headline1,
    selectedForeground: Color = if (ChefBookTheme.colors.isDark && selectedBackground.luminance() > 0.5F) Color.Black else Color.White,
    unselectedForeground: Color = ChefBookTheme.colors.foregroundSecondary,
    leftIcon: ImageVector? = null,
    leftIconModifier: Modifier = Modifier,
    rightIcon: ImageVector? = null,
    rightIconModifier: Modifier = Modifier,
    iconsSize: Dp = 24.dp,
    isSelected: Boolean = false,
    isEnabled: Boolean = true,
) {

    val pressed = remember { mutableStateOf(false) }

    val transition = updateTransition(isSelected, label = "isChecked")

    val background by transition.animateColor(label = "background") { selected ->
        if (selected) selectedBackground else unselectedBackground
    }

    val foreground by transition.animateColor(label = "foreground") { selected ->
        if (selected) selectedForeground else unselectedForeground
    }

    val baseModifier = if (isEnabled)
        modifier
            .scalingClickable(
                pressed = pressed,
                scaleFactor = 0.95F,
                onClick = onClick,
            )
    else
        modifier

    Box(
        modifier = baseModifier
            .clippedBackground(background, RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier =  Modifier.padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(leftIcon != null) {
                leftIcon?.let {
                    Icon(
                        imageVector = leftIcon,
                        tint = foreground,
                        modifier = leftIconModifier
                            .size(iconsSize)
                            .aspectRatio(1F),
                        contentDescription = null,
                    )
                }
            }
            AnimatedVisibility(text != null) {
                Text(
                    text = text.orEmpty(),
                    style = textStyle,
                    modifier = Modifier
                        .padding(start = if (leftIcon != null) 4.dp else 0.dp,),
                    color = foreground,
                    maxLines = 1,
                )
            }
            AnimatedVisibility(rightIcon != null) {
                rightIcon?.let {
                    Icon(
                        imageVector = rightIcon,
                        tint = foreground,
                        modifier = rightIconModifier
                            .size(iconsSize)
                            .aspectRatio(1F),
                        contentDescription = null,
                    )
                }
            }
        }
        Shading(pressed.value, Color.Black.copy(alpha = 0.1F))
    }
}
