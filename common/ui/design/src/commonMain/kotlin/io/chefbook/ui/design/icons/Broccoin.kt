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

private const val ViewportSize = 100F

val ChefBookIcons.Broccoin: ImageVector
  get() {
    return instance ?: Builder(
      name = "Broccoin",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = ViewportSize,
      viewportHeight = ViewportSize,
      autoMirror = true,
    )
      .path(
        fill = SolidColor(Color(0xFFfaa833)),
      ) {
        moveTo(50.0f, 50.0f)
        moveToRelative(-46.0f, 0.0f)
        arcToRelative(46.0f, 46.0f, 0.0f, true, true, 92.0f, 0.0f)
        arcToRelative(46.0f, 46.0f, 0.0f, true, true, -92.0f, 0.0f)
      }
      .path(
        fill = SolidColor(Color(0xFFfcda43)),
      ) {
        moveTo(50.0f, 8.0f)
        curveToRelative(23.16f, 0.0f, 42.0f, 18.84f, 42.0f, 42.0f)
        reflectiveCurveToRelative(-18.84f, 42.0f, -42.0f, 42.0f)
        reflectiveCurveTo(8.0f, 73.16f, 8.0f, 50.0f)
        reflectiveCurveTo(26.84f, 8.0f, 50.0f, 8.0f)
        moveTo(50.0f, 0.0f)
        curveTo(22.39f, 0.0f, 0.0f, 22.39f, 0.0f, 50.0f)
        reflectiveCurveToRelative(22.39f, 50.0f, 50.0f, 50.0f)
        reflectiveCurveToRelative(50.0f, -22.39f, 50.0f, -50.0f)
        reflectiveCurveTo(77.61f, 0.0f, 50.0f, 0.0f)
        horizontalLineToRelative(0.0f)
        close()
      }
      .path(
        fill = SolidColor(Color(0xFFfcda43)),
      ) {
        moveTo(44.64f, 25.0f)
        curveToRelative(0.5f, 0.0f, 1.0f, 0.02f, 1.51f, 0.07f)
        curveToRelative(4.11f, 0.37f, 7.67f, 2.18f, 10.03f, 4.77f)
        curveToRelative(1.24f, -0.48f, 2.55f, -0.79f, 3.88f, -0.91f)
        curveToRelative(0.44f, -0.04f, 0.87f, -0.06f, 1.3f, -0.06f)
        curveToRelative(5.83f, 0.0f, 10.67f, 3.51f, 11.08f, 8.26f)
        curveToRelative(0.08f, 0.95f, -0.02f, 1.9f, -0.29f, 2.82f)
        curveToRelative(2.12f, 0.8f, 3.71f, 2.27f, 4.25f, 4.2f)
        curveToRelative(1.08f, 3.81f, -2.31f, 7.97f, -7.55f, 9.3f)
        curveToRelative(-1.06f, 0.27f, -2.15f, 0.4f, -3.24f, 0.4f)
        horizontalLineToRelative(-0.12f)
        verticalLineToRelative(8.53f)
        curveToRelative(0.0f, 6.12f, -5.01f, 11.13f, -11.13f, 11.13f)
        horizontalLineToRelative(-8.69f)
        curveToRelative(-6.12f, 0.0f, -11.13f, -5.01f, -11.13f, -11.13f)
        verticalLineToRelative(-5.82f)
        curveToRelative(-5.34f, 0.0f, -9.68f, -4.38f, -9.68f, -9.78f)
        curveToRelative(0.0f, -4.22f, 2.65f, -7.81f, 6.36f, -9.18f)
        curveToRelative(-0.09f, -0.7f, -0.11f, -1.41f, -0.06f, -2.12f)
        curveToRelative(0.52f, -6.02f, 6.42f, -10.49f, 13.49f, -10.49f)
        moveTo(44.64f, 19.83f)
        curveToRelative(-2.26f, 0.0f, -4.5f, 0.35f, -6.64f, 1.05f)
        curveToRelative(-2.11f, 0.7f, -4.04f, 1.71f, -5.73f, 3.01f)
        curveToRelative(-1.76f, 1.36f, -3.2f, 2.98f, -4.27f, 4.82f)
        curveToRelative(-1.04f, 1.78f, -1.71f, 3.76f, -1.96f, 5.81f)
        curveToRelative(-0.19f, 0.13f, -0.38f, 0.27f, -0.56f, 0.41f)
        curveToRelative(-6.55f, 5.07f, -7.75f, 14.5f, -2.67f, 21.05f)
        curveToRelative(0.37f, 0.48f, 0.77f, 0.93f, 1.19f, 1.36f)
        curveToRelative(1.35f, 1.37f, 2.95f, 2.46f, 4.72f, 3.21f)
        curveToRelative(0.21f, 0.09f, 0.42f, 0.17f, 0.63f, 0.25f)
        verticalLineToRelative(1.58f)
        curveToRelative(0.0f, 2.18f, 0.44f, 4.34f, 1.29f, 6.35f)
        curveToRelative(1.66f, 3.9f, 4.77f, 7.01f, 8.67f, 8.67f)
        curveToRelative(2.01f, 0.85f, 4.17f, 1.29f, 6.35f, 1.29f)
        horizontalLineToRelative(8.7f)
        curveToRelative(2.18f, 0.0f, 4.34f, -0.44f, 6.35f, -1.29f)
        curveToRelative(3.9f, -1.66f, 7.01f, -4.77f, 8.67f, -8.67f)
        curveToRelative(0.85f, -2.01f, 1.29f, -4.17f, 1.29f, -6.35f)
        verticalLineToRelative(-4.06f)
        curveToRelative(0.88f, -0.25f, 1.73f, -0.57f, 2.56f, -0.94f)
        curveToRelative(0.97f, -0.44f, 1.89f, -0.97f, 2.76f, -1.58f)
        curveToRelative(0.85f, -0.6f, 1.64f, -1.28f, 2.36f, -2.04f)
        curveToRelative(0.72f, -0.76f, 1.34f, -1.6f, 1.86f, -2.5f)
        curveToRelative(0.74f, -1.28f, 1.22f, -2.63f, 1.44f, -4.01f)
        curveToRelative(0.39f, -2.48f, -0.11f, -5.02f, -1.43f, -7.16f)
        curveToRelative(-0.49f, -0.79f, -1.08f, -1.52f, -1.75f, -2.17f)
        curveToRelative(-0.27f, -0.26f, -0.54f, -0.5f, -0.84f, -0.74f)
        curveToRelative(0.0f, -0.16f, -0.02f, -0.32f, -0.03f, -0.48f)
        curveToRelative(-0.09f, -0.99f, -0.29f, -1.96f, -0.6f, -2.91f)
        curveToRelative(-0.31f, -0.92f, -0.71f, -1.8f, -1.21f, -2.62f)
        curveToRelative(-0.95f, -1.57f, -2.22f, -2.95f, -3.77f, -4.1f)
        curveToRelative(-1.47f, -1.08f, -3.14f, -1.93f, -4.96f, -2.5f)
        curveToRelative(-1.84f, -0.58f, -3.76f, -0.87f, -5.69f, -0.86f)
        curveToRelative(-0.58f, 0.0f, -1.17f, 0.03f, -1.76f, 0.08f)
        curveToRelative(-0.7f, 0.06f, -1.4f, 0.16f, -2.09f, 0.3f)
        curveToRelative(-0.08f, -0.06f, -0.16f, -0.12f, -0.24f, -0.18f)
        curveToRelative(-1.0f, -0.74f, -2.07f, -1.39f, -3.19f, -1.93f)
        curveToRelative(-2.32f, -1.12f, -4.84f, -1.81f, -7.47f, -2.05f)
        curveToRelative(-0.65f, -0.06f, -1.31f, -0.09f, -1.97f, -0.09f)
        horizontalLineToRelative(0.0f)
        close()
      }
      .path(
        fill = SolidColor(Color(0xFFfcda43)),
      ) {
        moveTo(56.57f, 57.78f)
        curveToRelative(0.83f, 0.0f, 1.48f, 0.81f, 1.32f, 1.68f)
        curveToRelative(-0.73f, 3.96f, -3.98f, 6.95f, -7.88f, 6.95f)
        reflectiveCurveToRelative(-7.15f, -2.99f, -7.88f, -6.95f)
        curveToRelative(-0.16f, -0.87f, 0.49f, -1.68f, 1.32f, -1.68f)
        horizontalLineToRelative(13.12f)
        close()
      }
      .path(
        fill = SolidColor(Color(0xFFfcda43)),
      ) {
        moveTo(41.73f, 47.43f)
        curveToRelative(-0.33f, 0.0f, -0.67f, 0.05f, -0.98f, 0.15f)
        curveToRelative(0.84f, 0.37f, 1.22f, 1.35f, 0.85f, 2.19f)
        reflectiveCurveToRelative(-1.35f, 1.22f, -2.19f, 0.85f)
        curveToRelative(-0.38f, -0.17f, -0.68f, -0.47f, -0.85f, -0.85f)
        curveToRelative(-0.54f, 1.76f, 0.44f, 3.62f, 2.19f, 4.16f)
        curveToRelative(1.76f, 0.54f, 3.62f, -0.44f, 4.16f, -2.19f)
        reflectiveCurveToRelative(-0.44f, -3.62f, -2.19f, -4.16f)
        curveToRelative(-0.32f, -0.1f, -0.65f, -0.15f, -0.98f, -0.15f)
        horizontalLineToRelative(0.0f)
        close()
      }
      .path(
        fill = SolidColor(Color(0xFFfcda43)),
      ) {
        moveTo(58.28f, 47.43f)
        curveToRelative(-0.33f, 0.0f, -0.67f, 0.05f, -0.98f, 0.15f)
        curveToRelative(0.84f, 0.37f, 1.22f, 1.35f, 0.85f, 2.19f)
        reflectiveCurveToRelative(-1.35f, 1.22f, -2.19f, 0.85f)
        curveToRelative(-0.38f, -0.17f, -0.68f, -0.47f, -0.85f, -0.85f)
        curveToRelative(-0.54f, 1.76f, 0.44f, 3.62f, 2.19f, 4.16f)
        curveToRelative(1.76f, 0.54f, 3.62f, -0.44f, 4.16f, -2.19f)
        curveToRelative(0.54f, -1.76f, -0.44f, -3.62f, -2.19f, -4.16f)
        curveToRelative(-0.32f, -0.1f, -0.65f, -0.15f, -0.98f, -0.15f)
        horizontalLineToRelative(0.0f)
        close()
      }
      .path(
        fill = SolidColor(Color(0xFF000000)),
        fillAlpha = 0.04f,
      ) {
        moveTo(100.0f, 50.0f)
        curveToRelative(0.0f, 27.61f, -22.39f, 50.0f, -50.0f, 50.0f)
        verticalLineTo(0.0f)
        curveToRelative(27.61f, 0.0f, 50.0f, 22.39f, 50.0f, 50.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private var instance: ImageVector? = null
