package io.chefbook.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.LockOpened: ImageVector
  get() {
    return instance ?: Builder(
      name = "LockOpened",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(12.0f, 13.0f)
        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
        reflectiveCurveToRelative(0.9f, 2.0f, 2.0f, 2.0f)
        reflectiveCurveToRelative(2.0f, -0.9f, 2.0f, -2.0f)
        reflectiveCurveToRelative(-0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(18.0f, 8.0f)
        horizontalLineToRelative(-1.0f)
        lineTo(17.0f, 6.0f)
        curveToRelative(0.0f, -2.76f, -2.24f, -5.0f, -5.0f, -5.0f)
        curveToRelative(-2.28f, 0.0f, -4.27f, 1.54f, -4.84f, 3.75f)
        curveToRelative(-0.14f, 0.54f, 0.18f, 1.08f, 0.72f, 1.22f)
        curveToRelative(0.53f, 0.14f, 1.08f, -0.18f, 1.22f, -0.72f)
        curveTo(9.44f, 3.93f, 10.63f, 3.0f, 12.0f, 3.0f)
        curveToRelative(1.65f, 0.0f, 3.0f, 1.35f, 3.0f, 3.0f)
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
        moveTo(18.0f, 19.0f)
        curveToRelative(0.0f, 0.55f, -0.45f, 1.0f, -1.0f, 1.0f)
        lineTo(7.0f, 20.0f)
        curveToRelative(-0.55f, 0.0f, -1.0f, -0.45f, -1.0f, -1.0f)
        verticalLineToRelative(-8.0f)
        curveToRelative(0.0f, -0.55f, 0.45f, -1.0f, 1.0f, -1.0f)
        horizontalLineToRelative(10.0f)
        curveToRelative(0.55f, 0.0f, 1.0f, 0.45f, 1.0f, 1.0f)
        verticalLineToRelative(8.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
