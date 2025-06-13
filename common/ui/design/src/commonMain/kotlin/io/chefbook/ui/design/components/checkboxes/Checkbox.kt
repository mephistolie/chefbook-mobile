//package io.chefbook.ui.design.components.checkboxes
//
//import androidx.compose.animation.animateColor
//import androidx.compose.animation.core.animateDp
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import io.chefbook.core.compose.constants.Hidden
//import io.chefbook.core.compose.constants.Visible
//import io.chefbook.core.compose.providers.theme.LocalTheme
//
//const val SelectedCheckboxBorderFactor = 1 / 2F
//const val UnselectedCheckboxBorderFactor = 1 / 12F
//const val CheckboxCornerRadiusFactor = 1F / 4
//
//@Composable
//fun Checkbox(
//  isChecked: Boolean,
//  onClick: () -> Unit,
//  checkmarkSize: Dp = 20.dp,
//  isEnabled: Boolean = true,
//) {
//
//  val colors = LocalTheme.colors
//
//  val transition = updateTransition(isChecked, label = "isChecked")
//
//  val borderWidth by transition.animateDp(label = "borderWidth") { checked ->
//    if (checked) size * SelectedCheckboxBorderFactor else size * UnselectedCheckboxBorderFactor
//  }
//
//  val alpha by transition.animateFloat(label = "alpha") { checked ->
//    if (checked) Visible else Hidden
//  }
//
//  val backgroundColor by transition.animateColor(label = "backgroundColor") { checked ->
//    if (checked) checkedColor else uncheckedColor
//  }
//
//  var baseModifier = Modifier
//    .size(size)
//    .clip(shape)
//  if (enabled) baseModifier = baseModifier.simpleClickable(onClick = onClick)
//
//
//  val checkboxModifier =
//    Modifier
//      .fillMaxSize()
//      .border(
//        BorderStroke(
//          width = borderWidth,
//          color = backgroundColor,
//        ),
//        shape = shape,
//      )
//
//  Box(
//    modifier = baseModifier,
//    contentAlignment = Alignment.Center
//  ) {
//    Box(
//      modifier = checkboxModifier
//    )
//    Icon(
//      imageVector = checkmarkIcon,
//      tint = checkmarkColor,
//      contentDescription = null,
//      modifier = Modifier
//        .size(checkmarkSize)
//        .alpha(alpha)
//    )
//  }
//}
