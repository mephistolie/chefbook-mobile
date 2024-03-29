package io.chefbook.design.components.bottomsheet

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FixedThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.chefbook.design.theme.shapes.BottomSheetShape
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

enum class AnimatedBottomSheetValue {
  HIDDEN,
  COLLAPSED,
  EXPANDED
}


interface BottomSheetExpandProgressProvider {
  val expandProgress: Float
}

@ExperimentalMaterialApi
@Stable
class TopBarBottomSheetState internal constructor(
  initialValue: AnimatedBottomSheetValue,
  val peekHeight: Animatable<Dp, AnimationVector1D>,
  animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
  confirmStateChange: (AnimatedBottomSheetValue) -> Boolean = { it != AnimatedBottomSheetValue.HIDDEN }
) : SwipeableState<AnimatedBottomSheetValue>(
  initialValue = initialValue,
  animationSpec = animationSpec,
  confirmStateChange = confirmStateChange
), BottomSheetExpandProgressProvider {
  val isExpanded: Boolean
    get() = currentValue == AnimatedBottomSheetValue.EXPANDED
  val isCollapsed: Boolean
    get() = currentValue == AnimatedBottomSheetValue.COLLAPSED
  val isHidden: Boolean
    get() = currentValue == AnimatedBottomSheetValue.HIDDEN
  val willExpanded: Boolean
    get() = targetValue == AnimatedBottomSheetValue.EXPANDED
  val willCollapsed: Boolean
    get() = targetValue == AnimatedBottomSheetValue.COLLAPSED
  val willHidden: Boolean
    get() = targetValue == AnimatedBottomSheetValue.HIDDEN
  val isStateChanging
    get() = targetValue != currentValue

  override val expandProgress: Float
    get() = when {
      !isAnimationRunning && direction > 0F && !isStateChanging && willExpanded -> 1F - progress.fraction
      !isAnimationRunning && direction < 0F && (isStateChanging || progress.fraction != 1F) -> progress.fraction
      !willExpanded -> 0F
      else -> 1F
    }

  val contentHeight = Animatable(0.dp, Dp.VectorConverter)

  suspend fun expand() = animateTo(AnimatedBottomSheetValue.EXPANDED)

  suspend fun collapse() = animateTo(AnimatedBottomSheetValue.COLLAPSED)

  suspend fun reopen(onHiddenCallback: () -> Unit = {}) {
    if (!isExpanded) {
      animateTo(AnimatedBottomSheetValue.HIDDEN)
      onHiddenCallback()
      animateTo(AnimatedBottomSheetValue.COLLAPSED)
    } else {
      onHiddenCallback()
    }
  }

  companion object {
    fun Saver(
      peekHeight: Animatable<Dp, AnimationVector1D>,
      animationSpec: AnimationSpec<Float>,
      confirmStateChange: (AnimatedBottomSheetValue) -> Boolean
    ): Saver<TopBarBottomSheetState, *> = Saver(
      save = { it.currentValue },
      restore = {
        TopBarBottomSheetState(
          initialValue = it,
          peekHeight = peekHeight,
          animationSpec = animationSpec,
          confirmStateChange = confirmStateChange
        )
      }
    )
  }

  internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection
}

@Composable
@ExperimentalMaterialApi
fun rememberTopBarBottomSheetState(
  initialValue: AnimatedBottomSheetValue = AnimatedBottomSheetValue.EXPANDED,
  peekHeight: Animatable<Dp, AnimationVector1D> = Animatable(0.dp, Dp.VectorConverter),
  animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
  confirmStateChange: (AnimatedBottomSheetValue) -> Boolean = { it != AnimatedBottomSheetValue.HIDDEN }
): TopBarBottomSheetState {
  return rememberSaveable(
    animationSpec,
    saver = TopBarBottomSheetState.Saver(
      peekHeight = peekHeight,
      animationSpec = animationSpec,
      confirmStateChange = confirmStateChange
    )
  ) {
    TopBarBottomSheetState(
      initialValue = initialValue,
      peekHeight = peekHeight,
      animationSpec = animationSpec,
      confirmStateChange = confirmStateChange
    )
  }
}

@Composable
@ExperimentalMaterialApi
fun TopBarBottomSheet(
  sheetContent: @Composable BoxScope.() -> Unit,
  modifier: Modifier = Modifier,
  sheetState: TopBarBottomSheetState = rememberTopBarBottomSheetState(),
  sheetShape: Shape = BottomSheetShape,
  floatingActionButton: (@Composable () -> Unit)? = null,
  floatingActionButtonPosition: FabPosition = FabPosition.End,
  backgroundColor: (@Composable (Float) -> Color) = { MaterialTheme.colors.background },
  topBarContent: @Composable (PaddingValues) -> Unit,
) {
  val density = LocalDensity.current
  val scope = rememberCoroutineScope()
  BoxWithConstraints(
    modifier = modifier
      .background(color = backgroundColor(sheetState.expandProgress))
      .statusBarsPadding()
      .fillMaxSize(),
  ) {
    val fullHeightPx = constraints.maxHeight.toFloat()
    val peekHeightPx = with(density) { sheetState.peekHeight.value.toPx() }
    val contentHeightPx = with(density) { sheetState.contentHeight.value.toPx() }

    val bottomSheetModifier = if (peekHeightPx > contentHeightPx) Modifier else Modifier
      .nestedScroll(sheetState.nestedScrollConnection)
    val swipeable = bottomSheetModifier
      .swipeable(
        state = sheetState,
        anchors = mapOf(
          fullHeightPx - 1F to AnimatedBottomSheetValue.HIDDEN,
          fullHeightPx - peekHeightPx to AnimatedBottomSheetValue.COLLAPSED,
          0F to AnimatedBottomSheetValue.EXPANDED,
        ),
        enabled = peekHeightPx > contentHeightPx,
        thresholds = { _, _ -> FixedThreshold(sheetState.peekHeight.value) },
        orientation = Orientation.Vertical,
        resistance = ResistanceConfig(
          basis = if (peekHeightPx > 0) peekHeightPx else 10F,
          factorAtMin = 5F,
          factorAtMax = 5F,
        ),
        velocityThreshold = Dp.Infinity
      )

    AnimatedBottomSheetScaffoldStack(
      body = {
        Surface(
          modifier = Modifier
            .onGloballyPositioned { coordinates ->
              scope.launch {
                val targetValue =
                  with(density) { fullHeightPx.toDp() - coordinates.size.height.toDp() }
                if (sheetState.peekHeight.targetValue != targetValue) {
                  scope.launch { sheetState.peekHeight.animateTo(targetValue) }
                }
              }
            }
        ) {
          topBarContent(PaddingValues(bottom = sheetState.peekHeight.value))
        }
      },
      bottomSheet = {
        Surface(
          swipeable
            .fillMaxWidth()
            .requiredHeightIn(min = sheetState.peekHeight.value),
          shape = sheetShape,
          color = Color.Transparent,
          content = {
            Box(contentAlignment = Alignment.BottomCenter) {
              Box(
                modifier = Modifier
                  .wrapContentHeight()
                  .animateContentSize()
                  .onGloballyPositioned {
                    scope.launch {
                      sheetState.contentHeight.animateTo(with(density) { it.size.height.toDp() })
                    }
                  },
                content = sheetContent,
                contentAlignment = Alignment.BottomCenter
              )
            }
          }
        )
      },
      floatingActionButton = {
        Box {
          floatingActionButton?.invoke()
        }
      },
      bottomSheetOffset = sheetState.offset,
      floatingActionButtonPosition = floatingActionButtonPosition
    )
  }
}

@Composable
private fun AnimatedBottomSheetScaffoldStack(
  body: @Composable () -> Unit,
  bottomSheet: @Composable () -> Unit,
  bottomSheetOffset: State<Float>,
  floatingActionButton: @Composable () -> Unit,
  floatingActionButtonPosition: FabPosition
) {
  Layout(
    content = {
      body()
      bottomSheet()
      floatingActionButton()
    }
  ) { measurables, constraints ->
    val placeable = measurables.first().measure(constraints)

    layout(placeable.width, placeable.height) {
      placeable.placeRelative(0, 0)

      val (sheetPlaceable, fabPlaceable) =
        measurables.drop(1).map {
          it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

      val sheetOffsetY = bottomSheetOffset.value.roundToInt()

      sheetPlaceable.placeRelative(0, sheetOffsetY)

      val fabOffsetX = when (floatingActionButtonPosition) {
        FabPosition.Center -> (placeable.width - fabPlaceable.width) / 2
        else -> placeable.width - fabPlaceable.width - FabEndSpacing.roundToPx()
      }
      val fabOffsetY = sheetOffsetY - fabPlaceable.height / 2

      fabPlaceable.placeRelative(fabOffsetX, fabOffsetY)
    }
  }
}

private val FabEndSpacing = 16.dp
