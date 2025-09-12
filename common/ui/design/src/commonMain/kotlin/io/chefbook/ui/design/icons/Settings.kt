package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.Settings: ImageVector
  get() {
    return instance ?: Builder(
      name = "Settings",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(12.0f, 23.0f)
        lineTo(2.5f, 17.5f)
        verticalLineTo(6.5f)
        lineTo(12.0f, 1.0f)
        lineTo(21.5f, 6.5f)
        verticalLineTo(17.5f)
        lineTo(12.0f, 23.0f)
        close()
        moveTo(12.0f, 3.312f)
        lineTo(4.5f, 7.653f)
        verticalLineTo(16.347f)
        lineTo(12.0f, 20.689f)
        lineTo(19.5f, 16.347f)
        verticalLineTo(7.653f)
        lineTo(12.0f, 3.311f)
        verticalLineTo(3.312f)
        close()
        moveTo(12.0f, 16.0f)
        curveTo(10.9395f, 15.997f, 9.9229f, 15.5759f, 9.171f, 14.828f)
        curveTo(8.0272f, 13.6839f, 7.6852f, 11.9635f, 8.3045f, 10.4689f)
        curveTo(8.9237f, 8.9744f, 10.3822f, 8.0f, 12.0f, 8.0f)
        curveTo(13.0603f, 8.0028f, 14.0765f, 8.424f, 14.828f, 9.172f)
        curveTo(16.3895f, 10.734f, 16.3895f, 13.266f, 14.828f, 14.828f)
        curveTo(14.0764f, 15.5757f, 13.0602f, 15.9968f, 12.0f, 16.0f)
        close()
        moveTo(12.0f, 10.0f)
        curveTo(11.0458f, 9.9998f, 10.2244f, 10.6736f, 10.0381f, 11.6094f)
        curveTo(9.8518f, 12.5452f, 10.3524f, 13.4823f, 11.2339f, 13.8476f)
        curveTo(12.1153f, 14.2129f, 13.1321f, 13.9047f, 13.6623f, 13.1114f)
        curveTo(14.1926f, 12.3182f, 14.0886f, 11.2608f, 13.414f, 10.586f)
        curveTo(13.0398f, 10.2098f, 12.5307f, 9.9988f, 12.0f, 10.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
