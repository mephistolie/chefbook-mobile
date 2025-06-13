package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.Eye: ImageVector
  get() {
    return instance ?: Builder(
      name = "Eye",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(12.0f, 4.5f)
        curveTo(7.0f, 4.5f, 2.73f, 7.61f, 1.0f, 12.0f)
        curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
        reflectiveCurveToRelative(9.27f, -3.11f, 11.0f, -7.5f)
        curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
        close()
        moveTo(12.0f, 17.0f)
        curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
        reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
        reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
        reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
        close()
        moveTo(12.0f, 9.0f)
        curveToRelative(-1.66f, 0.0f, -3.0f, 1.34f, -3.0f, 3.0f)
        reflectiveCurveToRelative(1.34f, 3.0f, 3.0f, 3.0f)
        reflectiveCurveToRelative(3.0f, -1.34f, 3.0f, -3.0f)
        reflectiveCurveToRelative(-1.34f, -3.0f, -3.0f, -3.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
