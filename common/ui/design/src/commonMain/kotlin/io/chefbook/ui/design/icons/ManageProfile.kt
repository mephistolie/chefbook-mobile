package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.ManageProfile: ImageVector
  get() {
    return instance ?: Builder(
      name = "ManageProfile",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(3.53f, 20.28f)
        lineTo(1.34f, 20.28f)
        arcToRelative(6.58f, 6.58f, 0.0f, false, true, 13.16f, 0.0f)
        lineTo(12.29f, 20.28f)
        arcToRelative(4.39f, 4.39f, 0.0f, false, false, -8.77f, 0.0f)
        close()
        moveTo(7.92f, 12.61f)
        arcTo(4.39f, 4.39f, 0.0f, true, true, 12.29f, 8.2f)
        verticalLineToRelative(0.0f)
        arcTo(4.41f, 4.41f, 0.0f, false, true, 7.92f, 12.61f)
        close()
        moveTo(7.92f, 6.0f)
        arcToRelative(2.19f, 2.19f, 0.0f, true, false, 2.19f, 2.29f)
        verticalLineToRelative(-0.1f)
        arcTo(2.19f, 2.19f, 0.0f, false, false, 7.92f, 6.0f)
        close()

      }
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(18.31f, 17.77f)
        lineToRelative(-4.5f, -2.61f)
        lineTo(13.81f, 10.0f)
        lineToRelative(4.5f, -2.61f)
        lineTo(22.81f, 10.0f)
        verticalLineToRelative(5.21f)
        close()
        moveTo(18.31f, 8.44f)
        lineTo(14.76f, 10.5f)
        verticalLineToRelative(4.11f)
        lineToRelative(3.55f, 2.06f)
        lineToRelative(3.55f, -2.06f)
        lineTo(21.86f, 10.5f)
        lineTo(18.31f, 8.44f)
        close()
        moveTo(18.31f, 14.44f)
        arcToRelative(1.9f, 1.9f, 0.0f, true, true, 1.34f, -0.56f)
        arcTo(1.89f, 1.89f, 0.0f, false, true, 18.31f, 14.45f)
        close()
        moveTo(18.31f, 11.6f)
        arcToRelative(0.95f, 0.95f, 0.0f, true, false, 0.79f, 1.47f)
        arcToRelative(0.95f, 0.95f, 0.0f, false, false, -0.79f, -1.47f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
