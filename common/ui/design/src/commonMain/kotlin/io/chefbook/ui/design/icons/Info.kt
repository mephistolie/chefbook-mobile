package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.Info: ImageVector
  get() {
    return instance ?: Builder(
      name = "Info",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(12.0f, 22.0f)
        curveTo(6.4771f, 22.0f, 2.0f, 17.5228f, 2.0f, 12.0f)
        curveTo(2.0f, 6.4771f, 6.4771f, 2.0f, 12.0f, 2.0f)
        curveTo(17.5228f, 2.0f, 22.0f, 6.4771f, 22.0f, 12.0f)
        curveTo(21.9939f, 17.5203f, 17.5203f, 21.9939f, 12.0f, 22.0f)
        close()
        moveTo(4.0f, 12.172f)
        curveTo(4.0473f, 16.5732f, 7.6411f, 20.1095f, 12.0425f, 20.086f)
        curveTo(16.444f, 20.0622f, 19.9995f, 16.4875f, 19.9995f, 12.086f)
        curveTo(19.9995f, 7.6845f, 16.444f, 4.1098f, 12.0425f, 4.086f)
        curveTo(7.6411f, 4.0625f, 4.0473f, 7.5988f, 4.0f, 12.0f)
        verticalLineTo(12.172f)
        close()
        moveTo(14.0f, 17.0f)
        horizontalLineTo(11.0f)
        verticalLineTo(13.0f)
        horizontalLineTo(10.0f)
        verticalLineTo(11.0f)
        horizontalLineTo(13.0f)
        verticalLineTo(15.0f)
        horizontalLineTo(14.0f)
        verticalLineTo(17.0f)
        close()
        moveTo(13.0f, 9.0f)
        horizontalLineTo(11.0f)
        verticalLineTo(7.0f)
        horizontalLineTo(13.0f)
        verticalLineTo(9.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
