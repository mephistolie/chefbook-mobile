package io.chefbook.ui.design.theme.shapes.smooth

import androidx.annotation.IntRange
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.min

/*
 * Copied from https://github.com/racra/smooth-corner-rect-android-compose
 */
class SmoothCornerShape(
  topStart: CornerSize,
  topEnd: CornerSize,
  bottomEnd: CornerSize,
  bottomStart: CornerSize,
  private val smoothness: Float = 0.6F,
) : CornerBasedShape(
  topStart = topStart,
  topEnd = topEnd,
  bottomEnd = bottomEnd,
  bottomStart = bottomStart,
) {

  private val cornersMap by lazy { mutableMapOf<String, SmoothCorner>() }

  override fun createOutline(
    size: Size,
    topStart: Float,
    topEnd: Float,
    bottomEnd: Float,
    bottomStart: Float,
    layoutDirection: LayoutDirection
  ) = when {
    topStart + topEnd + bottomEnd + bottomStart == 0.0f -> Outline.Rectangle(size.toRect())

    smoothness == 0F -> Outline.Rounded(
      RoundRect(
        rect = size.toRect(),
        topLeft = CornerRadius(topStart),
        topRight = CornerRadius(topEnd),
        bottomRight = CornerRadius(bottomEnd),
        bottomLeft = CornerRadius(bottomStart)
      )
    )

    else -> Outline.Generic(
      Path().apply {
        val halfOfShortestSide = min(size.height, size.width) / 2

        var selectedCorner = cornersMap.getOrPut("$topStart") {
          SmoothCorner(
            topStart,
            smoothness,
            halfOfShortestSide
          )
        }

        // Top Left Corner
        moveTo(
          selectedCorner.anchorPoint1.distanceToClosestSide,
          selectedCorner.anchorPoint1.distanceToFurthestSide
        )

        cubicTo(
          selectedCorner.controlPoint1.distanceToClosestSide,
          selectedCorner.controlPoint1.distanceToFurthestSide,
          selectedCorner.controlPoint2.distanceToClosestSide,
          selectedCorner.controlPoint2.distanceToFurthestSide,
          selectedCorner.anchorPoint2.distanceToClosestSide,
          selectedCorner.anchorPoint2.distanceToFurthestSide
        )

        arcToRad(
          rect = Rect(
            top = 0f,
            left = 0f,
            right = selectedCorner.arcSection.radius * 2,
            bottom = selectedCorner.arcSection.radius * 2
          ),
          startAngleRadians =
          (toRadians(180.0) + selectedCorner.arcSection.arcStartAngle)
            .toFloat(),
          sweepAngleRadians = selectedCorner.arcSection.arcSweepAngle,
          forceMoveTo = false
        )

        cubicTo(
          selectedCorner.controlPoint2.distanceToFurthestSide,
          selectedCorner.controlPoint2.distanceToClosestSide,
          selectedCorner.controlPoint1.distanceToFurthestSide,
          selectedCorner.controlPoint1.distanceToClosestSide,
          selectedCorner.anchorPoint1.distanceToFurthestSide,
          selectedCorner.anchorPoint1.distanceToClosestSide
        )

        selectedCorner = cornersMap.getOrPut("$topEnd") {
          SmoothCorner(
            topEnd,
            smoothness,
            halfOfShortestSide
          )
        }

        lineTo(
          size.width - selectedCorner.anchorPoint1.distanceToFurthestSide,
          selectedCorner.anchorPoint1.distanceToClosestSide
        )

        // Top Right Corner
        cubicTo(
          size.width - selectedCorner.controlPoint1.distanceToFurthestSide,
          selectedCorner.controlPoint1.distanceToClosestSide,
          size.width - selectedCorner.controlPoint2.distanceToFurthestSide,
          selectedCorner.controlPoint2.distanceToClosestSide,
          size.width - selectedCorner.anchorPoint2.distanceToFurthestSide,
          selectedCorner.anchorPoint2.distanceToClosestSide,
        )

        arcToRad(
          rect = Rect(
            top = 0f,
            left = size.width - selectedCorner.arcSection.radius * 2,
            right = size.width,
            bottom = selectedCorner.arcSection.radius * 2
          ),
          startAngleRadians =
          (toRadians(270.0) + selectedCorner.arcSection.arcStartAngle)
            .toFloat(),
          sweepAngleRadians = selectedCorner.arcSection.arcSweepAngle,
          forceMoveTo = false
        )

        cubicTo(
          size.width - selectedCorner.controlPoint2.distanceToClosestSide,
          selectedCorner.controlPoint2.distanceToFurthestSide,
          size.width - selectedCorner.controlPoint1.distanceToClosestSide,
          selectedCorner.controlPoint1.distanceToFurthestSide,
          size.width - selectedCorner.anchorPoint1.distanceToClosestSide,
          selectedCorner.anchorPoint1.distanceToFurthestSide,
        )

        selectedCorner = cornersMap.getOrPut("$bottomEnd") {
          SmoothCorner(
            bottomEnd,
            smoothness,
            halfOfShortestSide
          )
        }

        lineTo(
          size.width - selectedCorner.anchorPoint1.distanceToClosestSide,
          size.height - selectedCorner.anchorPoint1.distanceToFurthestSide
        )

        // Bottom Right Corner
        cubicTo(
          size.width - selectedCorner.controlPoint1.distanceToClosestSide,
          size.height - selectedCorner.controlPoint1.distanceToFurthestSide,
          size.width - selectedCorner.controlPoint2.distanceToClosestSide,
          size.height - selectedCorner.controlPoint2.distanceToFurthestSide,
          size.width - selectedCorner.anchorPoint2.distanceToClosestSide,
          size.height - selectedCorner.anchorPoint2.distanceToFurthestSide
        )

        arcToRad(
          rect = Rect(
            top = size.height - selectedCorner.arcSection.radius * 2,
            left = size.width - selectedCorner.arcSection.radius * 2,
            right = size.width,
            bottom = size.height
          ),
          startAngleRadians =
          (toRadians(0.0) + selectedCorner.arcSection.arcStartAngle)
            .toFloat(),
          sweepAngleRadians = selectedCorner.arcSection.arcSweepAngle,
          forceMoveTo = false
        )

        cubicTo(
          size.width - selectedCorner.controlPoint2.distanceToFurthestSide,
          size.height - selectedCorner.controlPoint2.distanceToClosestSide,
          size.width - selectedCorner.controlPoint1.distanceToFurthestSide,
          size.height - selectedCorner.controlPoint1.distanceToClosestSide,
          size.width - selectedCorner.anchorPoint1.distanceToFurthestSide,
          size.height - selectedCorner.anchorPoint1.distanceToClosestSide
        )

        selectedCorner = cornersMap.getOrPut("$bottomStart") {
          SmoothCorner(
            bottomStart,
            smoothness,
            halfOfShortestSide
          )
        }

        lineTo(
          selectedCorner.anchorPoint1.distanceToFurthestSide,
          size.height - selectedCorner.anchorPoint1.distanceToClosestSide
        )

        // Bottom Left Corner
        cubicTo(
          selectedCorner.controlPoint1.distanceToFurthestSide,
          size.height - selectedCorner.controlPoint1.distanceToClosestSide,
          selectedCorner.controlPoint2.distanceToFurthestSide,
          size.height - selectedCorner.controlPoint2.distanceToClosestSide,
          selectedCorner.anchorPoint2.distanceToFurthestSide,
          size.height - selectedCorner.anchorPoint2.distanceToClosestSide,
        )

        arcToRad(
          rect = Rect(
            top = size.height - selectedCorner.arcSection.radius * 2,
            left = 0f,
            right = selectedCorner.arcSection.radius * 2,
            bottom = size.height
          ),
          startAngleRadians =
          (toRadians(90.0) + selectedCorner.arcSection.arcStartAngle)
            .toFloat(),
          sweepAngleRadians = selectedCorner.arcSection.arcSweepAngle,
          forceMoveTo = false
        )

        cubicTo(
          selectedCorner.controlPoint2.distanceToClosestSide,
          size.height - selectedCorner.controlPoint2.distanceToFurthestSide,
          selectedCorner.controlPoint1.distanceToClosestSide,
          size.height - selectedCorner.controlPoint1.distanceToFurthestSide,
          selectedCorner.anchorPoint1.distanceToClosestSide,
          size.height - selectedCorner.anchorPoint1.distanceToFurthestSide
        )

        close()

      }
    )
  }

  override fun copy(
    topStart: CornerSize,
    topEnd: CornerSize,
    bottomEnd: CornerSize,
    bottomStart: CornerSize
  ) = SmoothCornerShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomEnd = bottomEnd,
    bottomStart = bottomStart,
    smoothness = smoothness,
  )
}

fun SmoothCornerShape(corner: CornerSize, smoothness: Float = 0.6F) =
  SmoothCornerShape(corner, corner, corner, corner, smoothness)

fun SmoothCornerShape(size: Dp, smoothness: Float = 0.6F) =
  SmoothCornerShape(CornerSize(size), smoothness)

fun SmoothCornerShape(size: Float, smoothness: Float = 0.6F) =
  SmoothCornerShape(CornerSize(size), smoothness)

fun SmoothCornerShape(percent: Int, smoothness: Float = 0.6F) =
  SmoothCornerShape(CornerSize(percent), smoothness)

fun SmoothCornerShape(
  topStart: Dp = 0.dp,
  topEnd: Dp = 0.dp,
  bottomEnd: Dp = 0.dp,
  bottomStart: Dp = 0.dp,
  smoothness: Float = 0.6F,
) = SmoothCornerShape(
  topStart = CornerSize(topStart),
  topEnd = CornerSize(topEnd),
  bottomEnd = CornerSize(bottomEnd),
  bottomStart = CornerSize(bottomStart),
  smoothness = smoothness,
)

fun SmoothCornerShape(
  topStart: Float = 0.0f,
  topEnd: Float = 0.0f,
  bottomEnd: Float = 0.0f,
  bottomStart: Float = 0.0f,
  smoothness: Float = 0.6F,
) = SmoothCornerShape(
  topStart = CornerSize(topStart),
  topEnd = CornerSize(topEnd),
  bottomEnd = CornerSize(bottomEnd),
  bottomStart = CornerSize(bottomStart),
  smoothness = smoothness,
)

fun SmoothCornerShape(
  @IntRange(from = 0, to = 100)
  topStartPercent: Int = 0,
  @IntRange(from = 0, to = 100)
  topEndPercent: Int = 0,
  @IntRange(from = 0, to = 100)
  bottomEndPercent: Int = 0,
  @IntRange(from = 0, to = 100)
  bottomStartPercent: Int = 0,
  smoothness: Float = 0.6F,
) = SmoothCornerShape(
  topStart = CornerSize(topStartPercent),
  topEnd = CornerSize(topEndPercent),
  bottomEnd = CornerSize(bottomEndPercent),
  bottomStart = CornerSize(bottomStartPercent),
  smoothness = smoothness,
)
