package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.GlassmorphicButton

@Composable
internal fun ActionsWidgetButton(
  preview: String?,
  onClick: () -> Unit,
  isPreviewLoaded: MutableState<Boolean>,
  modifier: Modifier = Modifier,
  horizontalPadding: Dp = 12.dp,
  text: String? = null,
  cornerRadius: Dp = 16.dp,
  @DrawableRes
  leftIconId: Int? = null,
  leftContent: @Composable (() -> Unit)? = null,
  @DrawableRes
  rightIconId: Int? = null,
  rightIconModifier: Modifier = Modifier,
  isSelected: Boolean = false,
) {
  val colors = LocalTheme.colors

  val transition = updateTransition(isPreviewLoaded.value, label = "isPreviewLoaded")

  val unselectedBackground by transition.animateColor(label = "unselectedBackground") { isLoaded ->
    if (isLoaded) colors.backgroundSecondary.copy(alpha = 0.5F) else colors.backgroundSecondary
  }

  val selectedBackground by transition.animateColor(label = "selectedBackground") { isLoaded ->
    if (isLoaded) colors.tintPrimary.copy(alpha = 0.5F) else colors.tintPrimary
  }


  GlassmorphicButton(
    onClick = onClick,
    image = preview,
    onImageLoadingResult = { isImageLoaded -> isPreviewLoaded.value = isImageLoaded },
    modifier = modifier,
    horizontalPadding = horizontalPadding,
    cornerRadius = cornerRadius,
    text = text,
    leftIcon = leftIconId?.let { ImageVector.vectorResource(it) },
    leftContent = leftContent,
    rightIcon = rightIconId?.let { ImageVector.vectorResource(it) },
    rightIconModifier = rightIconModifier,
    iconsSize = 18.dp,
    isSelected = isSelected,
    selectedBackground = selectedBackground,
    unselectedBackground = unselectedBackground,
    unselectedForeground = colors.foregroundPrimary,
  )
}
