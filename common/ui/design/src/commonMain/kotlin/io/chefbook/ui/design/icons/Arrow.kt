package io.chefbook.ui.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private const val MEDIUM_SIZE = 4 / 7F

private const val ANGLE_START = 0F
private const val ANGLE_UP = 90F
private const val ANGLE_END = 180F
private const val ANGLE_DOWN = -90F

val ChefBookIcons.ArrowStart: ImageVector
  get() = arrow(
    postfix = "Start",
    angle = ANGLE_START,
  )

val ChefBookIcons.ArrowUp: ImageVector
  get() = arrow(
    postfix = "Up",
    angle = ANGLE_UP,
  )

val ChefBookIcons.ArrowEnd: ImageVector
  get() = arrow(
    postfix = "End",
    angle = ANGLE_END,
  )

val ChefBookIcons.ArrowDown: ImageVector
  get() = arrow(
    postfix = "Down",
    angle = ANGLE_DOWN,
  )

val ChefBookIcons.ArrowStartMedium: ImageVector
  get() = arrow(
    postfix = "StartMedium",
    angle = ANGLE_START,
    scale = MEDIUM_SIZE,
  )

val ChefBookIcons.ArrowUpMedium: ImageVector
  get() = arrow(
    postfix = "UpMedium",
    angle = ANGLE_UP,
    scale = MEDIUM_SIZE,
  )

val ChefBookIcons.ArrowEndMedium: ImageVector
  get() = arrow(
    postfix = "EndMedium",
    angle = ANGLE_END,
    scale = MEDIUM_SIZE,
  )

val ChefBookIcons.ArrowDownMedium: ImageVector
  get() = arrow(
    postfix = "DownMedium",
    angle = ANGLE_DOWN,
    scale = MEDIUM_SIZE,
  )

private fun ChefBookIcons.arrow(
  postfix: String,
  angle: Float,
  scale: Float = 1F,
): ImageVector {
  val name = "Arrow$postfix"

  return instances[name] ?: Builder(
    name = name,
    defaultWidth = IconSizeDefault.dp,
    defaultHeight = IconSizeDefault.dp,
    viewportWidth = IconSizeDefault,
    viewportHeight = IconSizeDefault,
    autoMirror = true,
  ).addGroup(
    rotate = angle,
    pivotX = IconSizeDefault / 2,
    pivotY = IconSizeDefault / 2,
    scaleX = scale,
    scaleY = scale,
  ).path(
    fill = IconTintDefault,
  ) {
    moveTo(16.62f, 2.99f)
    curveToRelative(-0.49f, -0.49f, -1.28f, -0.49f, -1.77f, 0.0f)
    lineTo(6.54f, 11.3f)
    curveToRelative(-0.39f, 0.39f, -0.39f, 1.02f, 0.0f, 1.41f)
    lineToRelative(8.31f, 8.31f)
    curveToRelative(0.49f, 0.49f, 1.28f, 0.49f, 1.77f, 0.0f)
    reflectiveCurveToRelative(0.49f, -1.28f, 0.0f, -1.77f)
    lineTo(9.38f, 12.0f)
    lineToRelative(7.25f, -7.25f)
    curveToRelative(0.48f, -0.48f, 0.48f, -1.28f, -0.01f, -1.76f)
    close()
  }
    .build()
    .also { instances[name] = it }
}

private var instances: MutableMap<String, ImageVector> = mutableMapOf()
