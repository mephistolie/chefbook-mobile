package io.chefbook.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private const val ViewportSize = IconSizeDefault * 2

val ChefBookIcons.VK: ImageVector
  get() {
    return instance ?: Builder(
      name = "VK",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = ViewportSize,
      viewportHeight = ViewportSize,
      autoMirror = true,
    )
      .path(
        fill = IconTintDefault,
      ) {
        moveTo(45.763f, 35.202f)
        curveToRelative(-1.797f, -3.234f, -6.426f, -7.12f, -8.337f, -8.811f)
        curveToRelative(-0.523f, -0.463f, -0.579f, -1.264f, -0.103f, -1.776f)
        curveToRelative(3.647f, -3.919f, 6.564f, -8.422f, 7.568f, -11.143f)
        curveTo(45.334f, 12.27f, 44.417f, 11.0f, 43.125f, 11.0f)
        lineToRelative(-3.753f, 0.0f)
        curveToRelative(-1.237f, 0.0f, -1.961f, 0.444f, -2.306f, 1.151f)
        curveToRelative(-3.031f, 6.211f, -5.631f, 8.899f, -7.451f, 10.47f)
        curveToRelative(-1.019f, 0.88f, -2.608f, 0.151f, -2.608f, -1.188f)
        curveToRelative(0.0f, -2.58f, 0.0f, -5.915f, 0.0f, -8.28f)
        curveToRelative(0.0f, -1.147f, -0.938f, -2.075f, -2.095f, -2.075f)
        lineTo(18.056f, 11.0f)
        curveToRelative(-0.863f, 0.0f, -1.356f, 0.977f, -0.838f, 1.662f)
        lineToRelative(1.132f, 1.625f)
        curveToRelative(0.426f, 0.563f, 0.656f, 1.248f, 0.656f, 1.951f)
        lineTo(19.0f, 23.556f)
        curveToRelative(0.0f, 1.273f, -1.543f, 1.895f, -2.459f, 1.003f)
        curveToRelative(-3.099f, -3.018f, -5.788f, -9.181f, -6.756f, -12.128f)
        curveTo(9.505f, 11.578f, 8.706f, 11.002f, 7.8f, 11.0f)
        lineToRelative(-3.697f, -0.009f)
        curveToRelative(-1.387f, 0.0f, -2.401f, 1.315f, -2.024f, 2.639f)
        curveToRelative(3.378f, 11.857f, 10.309f, 23.137f, 22.661f, 24.36f)
        curveToRelative(1.217f, 0.12f, 2.267f, -0.86f, 2.267f, -2.073f)
        lineToRelative(0.0f, -3.846f)
        curveToRelative(0.0f, -1.103f, 0.865f, -2.051f, 1.977f, -2.079f)
        curveToRelative(0.039f, -0.001f, 0.078f, -0.001f, 0.117f, -0.001f)
        curveToRelative(3.267f, 0.0f, 6.926f, 4.755f, 8.206f, 6.979f)
        curveToRelative(0.368f, 0.64f, 1.056f, 1.03f, 1.8f, 1.03f)
        lineToRelative(4.973f, 0.0f)
        curveTo(45.531f, 38.0f, 46.462f, 36.461f, 45.763f, 35.202f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
