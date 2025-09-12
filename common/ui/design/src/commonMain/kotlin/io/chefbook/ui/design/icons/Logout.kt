package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.Logout: ImageVector
  get() {
    return instance ?: Builder(
      name = "Logout",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(18.0f, 21.0f)
        lineTo(9.0f, 21.0f)
        arcToRelative(2.0f, 2.0f, 0.0f, false, true, -2.0f, -2.0f)
        lineTo(7.0f, 15.0f)
        lineTo(9.0f, 15.0f)
        verticalLineToRelative(4.0f)
        horizontalLineToRelative(9.0f)
        lineTo(18.0f, 5.0f)
        lineTo(9.0f, 5.0f)
        lineTo(9.0f, 9.0f)
        lineTo(7.0f, 9.0f)
        lineTo(7.0f, 5.0f)
        arcTo(2.0f, 2.0f, 0.0f, false, true, 9.0f, 3.0f)
        horizontalLineToRelative(9.0f)
        arcToRelative(2.0f, 2.0f, 0.0f, false, true, 2.0f, 2.0f)
        lineTo(20.0f, 19.0f)
        arcTo(2.0f, 2.0f, 0.0f, false, true, 18.0f, 21.0f)
        close()
        moveTo(11.0f, 16.0f)
        lineTo(11.0f, 13.0f)
        lineTo(2.0f, 13.0f)
        lineTo(2.0f, 11.0f)
        horizontalLineToRelative(9.0f)
        lineTo(11.0f, 8.0f)
        lineToRelative(5.0f, 4.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
