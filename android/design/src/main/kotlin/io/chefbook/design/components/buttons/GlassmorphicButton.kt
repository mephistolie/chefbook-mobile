//package io.chefbook.design.components.buttons
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.animateColor
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.Stable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.BlurredEdgeTreatment
//import androidx.compose.ui.draw.blur
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.luminance
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.layout.ContentScale.Companion.Crop
//import androidx.compose.ui.layout.ContentScale.Companion.None
//import androidx.compose.ui.layout.ScaleFactor
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import io.chefbook.core.android.compose.constants.ShortDebounceInterval
//import io.chefbook.core.compose.constants.ShortDebounceInterval
//import io.chefbook.core.compose.providers.theme.LocalTheme
//import io.chefbook.design.components.images.EncryptedImage
//import io.chefbook.design.theme.dimens.IconSize24
//
//@Composable
//fun GlassmorphicButton(
//  onClick: () -> Unit,
//  image: String?,
//  modifier: Modifier = Modifier,
//  onImageLoadingResult: (Boolean) -> Unit = {},
//  blurRadius: Dp = 0.dp,
//  horizontalPadding: Dp = 10.dp,
//  cornerRadius: Dp = 16.dp,
//  selectedBackground: Color = LocalTheme.colors.tintPrimary,
//  unselectedBackground: Color = LocalTheme.colors.backgroundSecondary,
//  text: String? = null,
//  textStyle: TextStyle = LocalTheme.typography.headline1,
//  selectedForeground: Color = if (LocalTheme.colors.isDark && selectedBackground.luminance() > 0.5F) Color.Black else Color.White,
//  unselectedForeground: Color = LocalTheme.colors.foregroundSecondary,
//  leftIcon: ImageVector? = null,
//  leftContent: @Composable (() -> Unit)? = null,
//  leftIconModifier: Modifier = Modifier,
//  rightIcon: ImageVector? = null,
//  rightIconModifier: Modifier = Modifier,
//  iconsSize: Dp = IconSize24,
//  isSelected: Boolean = false,
//  isEnabled: Boolean = true,
//  disableScaling: Boolean = false,
//  debounceInterval: Long? = ShortDebounceInterval,
//) {
//  val transition = updateTransition(isSelected, label = "isChecked")
//
//  val background by transition.animateColor(label = "background") { selected ->
//    if (selected) selectedBackground else unselectedBackground
//  }
//
//  val foreground by transition.animateColor(label = "foreground") { selected ->
//    if (selected) selectedForeground else unselectedForeground
//  }
//
//  StandardButton(
//    onClick = onClick,
//    modifier = modifier,
//    cornerRadius = cornerRadius,
//    background = background,
//    isEnabled = isEnabled,
//    disableScaling = disableScaling,
//    debounceInterval = debounceInterval,
//  ) { isPressed ->
//    image?.let {
//      val pressedTransition = updateTransition(isPressed, label = "isChecked")
//      val imageScaling by pressedTransition.animateFloat(label = "imageScaling") { pressed ->
//        if (pressed) 1.1F else 1F
//      }
//
//      EncryptedImage(
//        data = image,
//        modifier = Modifier
//          .matchParentSize()
//          .scale(imageScaling)
//          .blur(blurRadius, BlurredEdgeTreatment.Unbounded),
//        colorFilter = ColorFilter.tint(background),
//        onSuccess = { onImageLoadingResult(true) },
//        onError = { onImageLoadingResult(false) },
//        contentScale = ContentScaleAtLeast,
//      )
//    }
//    Row(
//      modifier = Modifier.padding(horizontal = horizontalPadding),
//      verticalAlignment = Alignment.CenterVertically
//    ) {
//      AnimatedVisibility(leftIcon != null) {
//        leftIcon?.let {
//          Icon(
//            imageVector = leftIcon,
//            tint = foreground,
//            modifier = leftIconModifier
//              .size(iconsSize)
//              .aspectRatio(1F),
//            contentDescription = null,
//          )
//        }
//      }
//      leftContent?.invoke()
//      AnimatedVisibility(text != null) {
//        Text(
//          text = text.orEmpty(),
//          style = textStyle,
//          modifier = Modifier
//            .padding(start = if (leftIcon != null || leftContent != null) 4.dp else 0.dp),
//          color = foreground,
//          maxLines = 1,
//        )
//      }
//      AnimatedVisibility(rightIcon != null) {
//        rightIcon?.let {
//          Icon(
//            imageVector = rightIcon,
//            tint = foreground,
//            modifier = rightIconModifier
//              .size(iconsSize)
//              .aspectRatio(1F),
//            contentDescription = null,
//          )
//        }
//      }
//    }
//  }
//}
//
//@Stable
//private val ContentScaleAtLeast = object : ContentScale {
//  override fun computeScaleFactor(srcSize: Size, dstSize: Size): ScaleFactor =
//    when {
//      srcSize.width < dstSize.width || srcSize.height < dstSize.height -> Crop.computeScaleFactor(srcSize, dstSize)
//      else -> None.computeScaleFactor(srcSize, dstSize)
//    }
//}
