package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.LockClosed: ImageVector
  get() {
    return instance ?: Builder(
      name = "LockClosed",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(18.0f, 8.0f)
        horizontalLineToRelative(-1.0f)
        lineTo(17.0f, 6.0f)
        curveToRelative(0.0f, -2.76f, -2.24f, -5.0f, -5.0f, -5.0f)
        reflectiveCurveTo(7.0f, 3.24f, 7.0f, 6.0f)
        verticalLineToRelative(2.0f)
        lineTo(6.0f, 8.0f)
        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        verticalLineToRelative(10.0f)
        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(12.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        lineTo(20.0f, 10.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(9.0f, 6.0f)
        curveToRelative(0.0f, -1.66f, 1.34f, -3.0f, 3.0f, -3.0f)
        reflectiveCurveToRelative(3.0f, 1.34f, 3.0f, 3.0f)
        verticalLineToRelative(2.0f)
        lineTo(9.0f, 8.0f)
        lineTo(9.0f, 6.0f)
        close()
        moveTo(18.0f, 20.0f)
        lineTo(6.0f, 20.0f)
        lineTo(6.0f, 10.0f)
        horizontalLineToRelative(12.0f)
        verticalLineToRelative(10.0f)
        close()
        moveTo(12.0f, 17.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        reflectiveCurveToRelative(-0.9f, -2.0f, -2.0f, -2.0f)
        reflectiveCurveToRelative(-2.0f, 0.9f, -2.0f, 2.0f)
        reflectiveCurveToRelative(0.9f, 2.0f, 2.0f, 2.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
