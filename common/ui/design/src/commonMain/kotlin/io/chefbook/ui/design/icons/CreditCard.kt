package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.CreditCard: ImageVector
  get() {
    return instance ?: Builder(
      name = "CreditCard",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(20.0f, 20.0f)
        horizontalLineTo(4.0f)
        curveTo(2.895f, 20.0f, 2.0f, 19.105f, 2.0f, 18.0f)
        verticalLineTo(6.0f)
        curveTo(2.0f, 4.895f, 2.895f, 4.0f, 4.0f, 4.0f)
        horizontalLineTo(20.0f)
        curveTo(21.105f, 4.0f, 22.0f, 4.895f, 22.0f, 6.0f)
        verticalLineTo(18.0f)
        curveTo(22.0f, 19.105f, 21.105f, 20.0f, 20.0f, 20.0f)
        close()
        moveTo(4.0f, 12.0f)
        verticalLineTo(18.0f)
        horizontalLineTo(20.0f)
        verticalLineTo(12.0f)
        horizontalLineTo(4.0f)
        close()
        moveTo(4.0f, 6.0f)
        verticalLineTo(8.0f)
        horizontalLineTo(20.0f)
        verticalLineTo(6.0f)
        horizontalLineTo(4.0f)
        close()
        moveTo(13.0f, 16.0f)
        horizontalLineTo(6.0f)
        verticalLineTo(14.0f)
        horizontalLineTo(13.0f)
        verticalLineTo(16.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
