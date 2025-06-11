package io.chefbook.design.theme.shapes.smooth

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/*
 * Copied from https://github.com/racra/smooth-corner-rect-android-compose
 */

internal class SmoothCorner(
  cornerRadius: Float,
  smoothness: Float,
  maximumCurveStartDistanceFromVertex: Float
) {

  init {
    require(smoothness >= 0) {
      "The value for smoothness can never be negative."
    }
  }

  private val radius = min(cornerRadius, maximumCurveStartDistanceFromVertex)

  // Distance from the first point of the curvature to the vertex of the corner
  private val curveStartDistance =
    min(maximumCurveStartDistanceFromVertex, (1 + smoothness) * radius)

  private val shouldCurveInterpolate = radius <= maximumCurveStartDistanceFromVertex / 2

  // This value is used to start interpolating between smooth corners and
  // round corners
  private val interpolationMultiplier =
    (radius - maximumCurveStartDistanceFromVertex / 2) / (maximumCurveStartDistanceFromVertex / 2)

  // Angle at second control point of the bezier curves
  private val angleAlpha =
    if (shouldCurveInterpolate)
      toRadians(45.0 * smoothness).toFloat()
    else
      toRadians(45.0 * smoothness * (1 - interpolationMultiplier)).toFloat()

  // Angle which dictates how much of the curve is going to be a slice of a circle
  private val angleBeta =
    if (shouldCurveInterpolate)
      toRadians(90.0 * (1.0 - smoothness)).toFloat()
    else
      toRadians(90.0 * (1 - smoothness * (1 - interpolationMultiplier))).toFloat()

  private val angleTheta = ((toRadians(90.0) - angleBeta) / 2.0).toFloat()

  // Distance from second control point to end of Bezier curves
  private val distanceE = radius * tan(angleTheta / 2)

  // Distances in the x and y axis used to position end of Bezier
  // curves relative to it's second control point
  private val distanceC = distanceE * cos(angleAlpha)
  private val distanceD = distanceC * tan(angleAlpha)

  // Distances used to position second control point of Bezier curves
  // relative to their first control point
  private val distanceK = sin(angleBeta / 2) * radius
  private val distanceL = (distanceK * sqrt(2.0)).toFloat()
  private val distanceB =
    ((curveStartDistance - distanceL) - (1 + tan(angleAlpha)) * distanceC) / 3

  // Distance used to position first control point of Bezier curves
  // relative to their origin
  private val distanceA = 2 * distanceB

  // Represents the outer anchor points of the smooth curve
  val anchorPoint1 = PointRelativeToVertex(
    min(curveStartDistance, maximumCurveStartDistanceFromVertex),
    0f
  )

  // Represents the control point for point1
  val controlPoint1 = PointRelativeToVertex(
    anchorPoint1.distanceToFurthestSide - distanceA,
    0f
  )

  // Represents the control point for point2
  val controlPoint2 = PointRelativeToVertex(
    controlPoint1.distanceToFurthestSide - distanceB,
    0f
  )

  // Represents the inner anchor points of the smooth curve
  val anchorPoint2 = PointRelativeToVertex(
    controlPoint2.distanceToFurthestSide - distanceC,
    distanceD
  )

  val arcSection = Arc(
    radius = radius,
    arcStartAngle = angleTheta,
    arcSweepAngle = angleBeta
  )
}

/**
 * Represents a point positioned relative to a corner vertex, so that it can be used
 * to calculate a smooth curve independently of which quadrant of the rectangle this
 * curve will be inserted in.
 */
internal data class PointRelativeToVertex(
  val distanceToFurthestSide: Float,
  val distanceToClosestSide: Float
)

/**
 * Represents the arc section of a smooth corner curve
 *
 * @param arcStartAngle the start angle of the arc inside the first quadrant of rotation (0º to 90º)
 * @param arcSweepAngle the angle at the center point between the start and end of the arc
 */
internal data class Arc(
  val radius: Float,
  val arcStartAngle: Float,
  val arcSweepAngle: Float
)

internal fun toRadians(deg: Double): Double = deg / 180.0 * PI

internal fun toDegrees(rad: Double): Double = rad * 180.0 / PI
