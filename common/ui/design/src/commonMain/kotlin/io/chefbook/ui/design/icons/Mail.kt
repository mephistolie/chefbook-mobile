package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.Mail: ImageVector
  get() {
    return instance ?: Builder(
      name = "Mail",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(20.0f, 4.0f)
        lineTo(4.0f, 4.0f)
        curveToRelative(-1.1f, 0.0f, -1.99f, 0.9f, -1.99f, 2.0f)
        lineTo(2.0f, 18.0f)
        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(16.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        lineTo(22.0f, 6.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        close()
        moveTo(19.6f, 8.25f)
        lineToRelative(-7.07f, 4.42f)
        curveToRelative(-0.32f, 0.2f, -0.74f, 0.2f, -1.06f, 0.0f)
        lineTo(4.4f, 8.25f)
        curveToRelative(-0.25f, -0.16f, -0.4f, -0.43f, -0.4f, -0.72f)
        curveToRelative(0.0f, -0.67f, 0.73f, -1.07f, 1.3f, -0.72f)
        lineTo(12.0f, 11.0f)
        lineToRelative(6.7f, -4.19f)
        curveToRelative(0.57f, -0.35f, 1.3f, 0.05f, 1.3f, 0.72f)
        curveToRelative(0.0f, 0.29f, -0.15f, 0.56f, -0.4f, 0.72f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
