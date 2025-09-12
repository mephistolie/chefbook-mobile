package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.User: ImageVector
  get() {
    return instance ?: Builder(
      name = "User",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(7.0f, 8.0f)
        curveTo(7.0f, 5.2386f, 9.2386f, 3.0f, 12.0f, 3.0f)
        curveTo(14.7614f, 3.0f, 17.0f, 5.2386f, 17.0f, 8.0f)
        curveTo(17.0f, 10.7614f, 14.7614f, 13.0f, 12.0f, 13.0f)
        curveTo(9.2386f, 13.0f, 7.0f, 10.7614f, 7.0f, 8.0f)
        close()
        moveTo(12.0f, 11.0f)
        curveTo(13.6569f, 11.0f, 15.0f, 9.6568f, 15.0f, 8.0f)
        curveTo(15.0f, 6.3432f, 13.6569f, 5.0f, 12.0f, 5.0f)
        curveTo(10.3431f, 5.0f, 9.0f, 6.3432f, 9.0f, 8.0f)
        curveTo(9.0f, 9.6568f, 10.3431f, 11.0f, 12.0f, 11.0f)
        close()
      }
      .path(
        fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
        pathFillType = NonZero
      ) {
        moveTo(6.3432f, 16.3431f)
        curveTo(4.8429f, 17.8434f, 4.0f, 19.8783f, 4.0f, 22.0f)
        horizontalLineTo(6.0f)
        curveTo(6.0f, 20.4087f, 6.6321f, 18.8826f, 7.7574f, 17.7574f)
        curveTo(8.8826f, 16.6321f, 10.4087f, 16.0f, 12.0f, 16.0f)
        curveTo(13.5913f, 16.0f, 15.1174f, 16.6321f, 16.2426f, 17.7574f)
        curveTo(17.3679f, 18.8826f, 18.0f, 20.4087f, 18.0f, 22.0f)
        horizontalLineTo(20.0f)
        curveTo(20.0f, 19.8783f, 19.1571f, 17.8434f, 17.6569f, 16.3431f)
        curveTo(16.1566f, 14.8429f, 14.1217f, 14.0f, 12.0f, 14.0f)
        curveTo(9.8783f, 14.0f, 7.8434f, 14.8429f, 6.3432f, 16.3431f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
