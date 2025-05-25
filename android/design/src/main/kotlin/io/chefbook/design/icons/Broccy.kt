package io.chefbook.design.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.chefbook.design.theme.colors.DeepOrangeLight

private const val ViewportSize = 2032.2F

private val BackgroundColor = SolidColor(Color.White)
private val TintColor = SolidColor(DeepOrangeLight)

val ChefBookIcons.Broccy: ImageVector
  get() {
    return instance ?: Builder(
      name = "Broccy",
      defaultWidth = IconSizeDefault.dp,
      defaultHeight = IconSizeDefault.dp,
      viewportWidth = ViewportSize,
      viewportHeight = ViewportSize,
      autoMirror = true,
    )
      .broccyStroke()
      .path(
        fill = TintColor,
      ) {
        moveTo(1207.2f, 1294.7f)
        curveToRelative(27.1f, 0.0f, 48.5f, 26.4f, 43.3f, 54.9f)
        curveToRelative(-23.8f, 129.6f, -130.2f, 227.3f, -257.9f, 227.3f)
        reflectiveCurveToRelative(-234.1f, -97.7f, -257.9f, -227.3f)
        curveToRelative(-5.3f, -28.6f, 16.1f, -54.9f, 43.3f, -54.9f)
        horizontalLineToRelative(429.3f)
        close()
      }
      .path(
        fill = TintColor,
      ) {
        moveTo(721.7f, 956.0f)
        curveToRelative(-10.9f, 0.0f, -21.8f, 1.6f, -32.2f, 4.8f)
        curveToRelative(27.5f, 12.1f, 39.9f, 44.3f, 27.8f, 71.8f)
        reflectiveCurveToRelative(-44.3f, 39.9f, -71.8f, 27.8f)
        curveToRelative(-12.4f, -5.5f, -22.3f, -15.4f, -27.8f, -27.8f)
        curveToRelative(-17.8f, 57.4f, 14.3f, 118.4f, 71.8f, 136.2f)
        curveToRelative(57.4f, 17.8f, 118.4f, -14.3f, 136.2f, -71.8f)
        reflectiveCurveToRelative(-14.3f, -118.4f, -71.8f, -136.2f)
        curveToRelative(-10.4f, -3.2f, -21.3f, -4.9f, -32.2f, -4.9f)
        horizontalLineToRelative(0.0f)
        close()
      }
      .path(
        fill = TintColor,
      ) {
        moveTo(1263.4f, 956.0f)
        curveToRelative(-10.9f, 0.0f, -21.8f, 1.6f, -32.2f, 4.8f)
        curveToRelative(27.5f, 12.2f, 39.9f, 44.3f, 27.8f, 71.8f)
        curveToRelative(-12.2f, 27.5f, -44.3f, 39.9f, -71.8f, 27.8f)
        curveToRelative(-12.4f, -5.5f, -22.3f, -15.4f, -27.8f, -27.8f)
        curveToRelative(-17.8f, 57.4f, 14.3f, 118.4f, 71.8f, 136.2f)
        curveToRelative(57.4f, 17.8f, 118.4f, -14.3f, 136.2f, -71.8f)
        curveToRelative(17.8f, -57.4f, -14.3f, -118.4f, -71.8f, -136.2f)
        curveToRelative(-10.4f, -3.2f, -21.3f, -4.9f, -32.2f, -4.9f)
        horizontalLineToRelative(0.0f)
        close()
      }
      .build()
      .also { instance = it }
  }

private fun Builder.broccyStroke(): Builder {
  return path(
    fill = BackgroundColor,
  ) {
    moveTo(1856.3f, 848.8f)
    curveToRelative(-17.9f, -63.2f, -69.9f, -111.1f, -139.1f, -137.3f)
    curveToRelative(9.0f, -29.8f, 12.2f, -61.1f, 9.6f, -92.2f)
    curveToRelative(-14.4f, -166.9f, -195.8f, -287.1f, -405.1f, -268.5f)
    curveToRelative(-43.5f, 3.9f, -86.3f, 13.9f, -127.0f, 29.6f)
    curveToRelative(-77.1f, -84.6f, -193.6f, -144.0f, -328.2f, -156.0f)
    curveToRelative(-252.9f, -22.4f, -472.7f, 130.3f, -490.9f, 341.2f)
    curveToRelative(-1.9f, 23.1f, -1.3f, 46.4f, 1.9f, 69.3f)
    curveToRelative(-121.3f, 44.8f, -208.0f, 162.2f, -208.0f, 300.3f)
    curveToRelative(0.0f, 176.7f, 141.8f, 320.0f, 316.6f, 320.0f)
    verticalLineToRelative(190.3f)
    curveToRelative(0.0f, 200.4f, 164.0f, 364.3f, 364.4f, 364.3f)
    horizontalLineToRelative(284.5f)
    curveToRelative(200.4f, 0.0f, 364.4f, -164.0f, 364.4f, -364.3f)
    verticalLineToRelative(-279.2f)
    curveToRelative(37.0f, 0.3f, 74.0f, -4.1f, 109.9f, -13.2f)
    curveToRelative(171.7f, -43.6f, 282.4f, -179.9f, 247.2f, -304.4f)
    close()
  }
    .path(
      fill = TintColor,
    ) {
      moveTo(817.0f, 222.3f)
      curveToRelative(16.2f, 0.0f, 32.7f, 0.7f, 49.3f, 2.2f)
      curveToRelative(134.6f, 12.0f, 251.1f, 71.4f, 328.2f, 156.0f)
      curveToRelative(40.7f, -15.8f, 83.5f, -25.8f, 127.0f, -29.6f)
      curveToRelative(14.3f, -1.3f, 28.5f, -1.9f, 42.6f, -1.9f)
      curveToRelative(190.8f, 0.0f, 349.1f, 115.0f, 362.5f, 270.4f)
      curveToRelative(2.6f, 31.0f, -0.6f, 62.3f, -9.6f, 92.2f)
      curveToRelative(69.3f, 26.3f, 121.3f, 74.2f, 139.1f, 137.3f)
      curveToRelative(35.2f, 124.5f, -75.5f, 260.8f, -247.2f, 304.4f)
      curveToRelative(-34.7f, 8.8f, -70.3f, 13.2f, -106.0f, 13.2f)
      horizontalLineToRelative(-3.9f)
      verticalLineToRelative(279.1f)
      curveToRelative(0.0f, 200.4f, -164.0f, 364.3f, -364.4f, 364.3f)
      horizontalLineToRelative(-284.5f)
      curveToRelative(-200.4f, 0.0f, -364.3f, -163.9f, -364.3f, -364.3f)
      verticalLineToRelative(-190.4f)
      curveToRelative(-174.9f, 0.0f, -316.6f, -143.3f, -316.6f, -320.0f)
      curveToRelative(0.0f, -138.1f, 86.7f, -255.4f, 208.0f, -300.3f)
      curveToRelative(-3.1f, -23.0f, -3.7f, -46.2f, -1.9f, -69.3f)
      curveToRelative(17.0f, -197.0f, 210.0f, -343.3f, 441.6f, -343.3f)
      moveTo(817.0f, 52.9f)
      curveToRelative(-73.8f, -0.2f, -147.2f, 11.5f, -217.3f, 34.4f)
      curveToRelative(-69.1f, 22.8f, -132.1f, 55.9f, -187.4f, 98.6f)
      curveToRelative(-57.7f, 44.5f, -104.6f, 97.6f, -139.6f, 157.7f)
      curveToRelative(-34.2f, 58.3f, -56.0f, 123.0f, -64.1f, 190.1f)
      curveToRelative(-6.2f, 4.4f, -12.4f, 8.9f, -18.4f, 13.6f)
      curveTo(-24.2f, 713.4f, -63.3f, 1021.7f, 102.7f, 1236.0f)
      curveToRelative(12.1f, 15.6f, 25.1f, 30.5f, 39.0f, 44.5f)
      curveToRelative(44.2f, 44.8f, 96.6f, 80.5f, 154.5f, 105.2f)
      curveToRelative(6.8f, 2.9f, 13.6f, 5.6f, 20.5f, 8.2f)
      verticalLineToRelative(51.6f)
      curveToRelative(-0.1f, 71.4f, 14.3f, 142.0f, 42.2f, 207.7f)
      curveToRelative(54.4f, 127.7f, 156.1f, 229.4f, 283.8f, 283.8f)
      curveToRelative(65.7f, 28.0f, 136.3f, 42.3f, 207.7f, 42.2f)
      horizontalLineToRelative(284.5f)
      curveToRelative(71.4f, 0.1f, 142.0f, -14.3f, 207.7f, -42.2f)
      curveToRelative(127.7f, -54.4f, 229.4f, -156.1f, 283.8f, -283.8f)
      curveToRelative(28.0f, -65.7f, 42.3f, -136.3f, 42.2f, -207.7f)
      verticalLineToRelative(-133.0f)
      curveToRelative(28.7f, -8.2f, 56.7f, -18.5f, 83.8f, -30.9f)
      curveToRelative(31.6f, -14.4f, 61.9f, -31.7f, 90.3f, -51.7f)
      curveToRelative(27.9f, -19.6f, 53.7f, -42.0f, 77.1f, -66.8f)
      curveToRelative(23.4f, -24.8f, 43.9f, -52.2f, 61.0f, -81.7f)
      curveToRelative(24.1f, -41.8f, 40.0f, -86.0f, 47.1f, -131.3f)
      curveToRelative(12.9f, -81.2f, -3.7f, -164.4f, -46.9f, -234.4f)
      curveToRelative(-16.0f, -26.0f, -35.3f, -49.8f, -57.2f, -71.0f)
      curveToRelative(-8.7f, -8.4f, -17.8f, -16.5f, -27.3f, -24.1f)
      curveToRelative(-0.3f, -5.3f, -0.6f, -10.5f, -1.1f, -15.8f)
      curveToRelative(-2.8f, -32.3f, -9.4f, -64.3f, -19.6f, -95.1f)
      curveToRelative(-10.0f, -30.0f, -23.3f, -58.7f, -39.6f, -85.8f)
      curveToRelative(-31.1f, -51.4f, -72.6f, -96.6f, -123.5f, -134.3f)
      curveToRelative(-48.0f, -35.5f, -102.6f, -63.0f, -162.3f, -81.9f)
      curveToRelative(-60.3f, -18.9f, -123.0f, -28.4f, -186.2f, -28.2f)
      curveToRelative(-19.1f, 0.0f, -38.4f, 0.9f, -57.5f, 2.5f)
      curveToRelative(-22.9f, 2.0f, -45.7f, 5.3f, -68.3f, 9.8f)
      curveToRelative(-2.7f, -2.0f, -5.3f, -4.0f, -8.0f, -6.0f)
      curveToRelative(-32.8f, -24.2f, -67.8f, -45.3f, -104.5f, -63.1f)
      curveToRelative(-76.0f, -36.8f, -158.3f, -59.4f, -244.5f, -67.0f)
      curveToRelative(-21.3f, -1.9f, -43.0f, -2.9f, -64.3f, -2.9f)
      horizontalLineToRelative(0.0f)
      close()
    }
}

private var instance: ImageVector? = null
