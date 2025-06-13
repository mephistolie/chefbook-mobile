package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChefBookIcons.Google: ImageVector
  get() {
    return instance ?: Builder(
      name = "Google",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = IconSizeDefault,
      viewportHeight = IconSizeDefault,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(11.956f, 10.356f)
        verticalLineTo(13.807f)
        horizontalLineTo(16.748f)
        curveTo(16.302f, 16.0f, 14.435f, 17.26f, 11.956f, 17.26f)
        curveTo(9.0685f, 17.2202f, 6.7486f, 14.8682f, 6.7486f, 11.9805f)
        curveTo(6.7486f, 9.0927f, 9.0685f, 6.7407f, 11.956f, 6.701f)
        curveTo(13.1562f, 6.6995f, 14.3194f, 7.116f, 15.246f, 7.879f)
        lineTo(17.846f, 5.279f)
        curveTo(14.8636f, 2.657f, 10.508f, 2.3198f, 7.1575f, 4.4514f)
        curveTo(3.8071f, 6.583f, 2.267f, 10.6712f, 3.3782f, 14.4836f)
        curveTo(4.4894f, 18.296f, 7.9849f, 20.9164f, 11.956f, 20.914f)
        curveTo(16.423f, 20.914f, 20.485f, 17.665f, 20.485f, 11.98f)
        curveTo(20.4781f, 11.4326f, 20.411f, 10.8877f, 20.285f, 10.355f)
        lineTo(11.956f, 10.356f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
