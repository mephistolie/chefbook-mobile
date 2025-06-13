package io.chefbook.ui.utils.compose.modifiers.clickable

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import io.chefbook.ui.utils.compose.constants.MediumDebounceInterval
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Configure component to receive clicks  or accessibility "click" event with debounce timeout.
 *
 * @param enabled Controls the enabled state. When `false`, [onClick], and this modifier will
 * appear disabled for accessibility services
 * @param onClickLabel semantic / accessibility label for the [onClick] action
 * @param role the type of user interface element. Accessibility services might use this
 * to describe the element or do customizations
 * @param debounceInterval minimum interval between two successful clicks. If null, standard clickable
 * will be used
 * @param onClick will be called when the element is successfully clicked
 **/
fun Modifier.debounceClickable(
  enabled: Boolean = true,
  onClickLabel: String? = null,
  role: Role? = null,
  debounceInterval: Long? = MediumDebounceInterval,
  onClick: () -> Unit,
): Modifier = composed {
  debounceClickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = LocalIndication.current,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    debounceInterval = debounceInterval,
    onClick = onClick,
  )
}

/**
 * Configure component to receive clicks  or accessibility "click" event with debounce timeout.
 *
 * @param interactionSource [MutableInteractionSource] that will be used to dispatch
 * [PressInteraction.Press] when this clickable is pressed. Only the initial (first) press will be
 * recorded and dispatched with [MutableInteractionSource].
 * @param indication indication to be shown when modified element is pressed. Be default,
 * indication from [LocalIndication] will be used. Pass `null` to show no indication, or
 * current value from [LocalIndication] to show theme default
 * @param enabled Controls the enabled state. When `false`, [onClick], and this modifier will
 * appear disabled for accessibility services
 * @param onClickLabel semantic / accessibility label for the [onClick] action
 * @param role the type of user interface element. Accessibility services might use this
 * to describe the element or do customizations
 * @param debounceInterval minimum interval between two successful clicks. If null, standard clickable
 * will be used
 * @param onClick will be called when the element is successfully clicked
 **/
@OptIn(ExperimentalTime::class)
@Suppress("LabeledExpression")
fun Modifier.debounceClickable(
  interactionSource: MutableInteractionSource,
  indication: Indication?,
  enabled: Boolean = true,
  onClickLabel: String? = null,
  role: Role? = null,
  debounceInterval: Long? = MediumDebounceInterval,
  onClick: () -> Unit,
): Modifier = composed {
  var lastClickTime by remember { mutableStateOf(0L) }
  clickable(
    interactionSource = interactionSource,
    indication = indication,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role
  ) {
    if (debounceInterval != null) {
      val currentTime = Clock.System.now().toEpochMilliseconds()
      if (currentTime - lastClickTime < debounceInterval) return@clickable
      lastClickTime = currentTime
    }
    onClick()
  }
}
