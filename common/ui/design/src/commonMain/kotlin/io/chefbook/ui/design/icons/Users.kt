package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.Users: ImageVector
  get() {
    return instance ?: Builder(
      name = "Users",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
        strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
          StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
      ) {
        moveTo(21.0f, 20.0f)
        curveTo(21.0f, 18.258f, 19.33f, 16.777f, 17.0f, 16.228f)
        moveTo(15.0f, 20.0f)
        curveTo(15.0f, 17.791f, 12.314f, 16.0f, 9.0f, 16.0f)
        curveTo(5.686f, 16.0f, 3.0f, 17.791f, 3.0f, 20.0f)
        moveTo(15.0f, 13.0f)
        curveTo(17.209f, 13.0f, 19.0f, 11.209f, 19.0f, 9.0f)
        curveTo(19.0f, 6.791f, 17.209f, 5.0f, 15.0f, 5.0f)
        moveTo(9.0f, 13.0f)
        curveTo(6.791f, 13.0f, 5.0f, 11.209f, 5.0f, 9.0f)
        curveTo(5.0f, 6.791f, 6.791f, 5.0f, 9.0f, 5.0f)
        curveTo(11.209f, 5.0f, 13.0f, 6.791f, 13.0f, 9.0f)
        curveTo(13.0f, 11.209f, 11.209f, 13.0f, 9.0f, 13.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
