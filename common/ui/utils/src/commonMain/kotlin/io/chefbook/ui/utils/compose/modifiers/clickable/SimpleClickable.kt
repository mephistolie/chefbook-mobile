package io.chefbook.ui.utils.compose.modifiers.clickable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import io.chefbook.ui.utils.compose.constants.MediumDebounceInterval

/**
 * Configure component to receive clicks  or accessibility "click" event without press effect.
 *
 * @param enabled Controls the enabled state. When `false`, [onClick], and this modifier will
 * appear disabled for accessibility services
 * @param onClickLabel semantic / accessibility label for the [onClick] action
 * @param role the type of user interface element. Accessibility services might use this
 * to describe the element or do customizations
 * @param debounceInterval minimum interval between two successful clicks. If null, standard clickable
 * will be used
 * @param onClick will be called when user clicks on the element
 */
fun Modifier.simpleClickable(
  enabled: Boolean = true,
  onClickLabel: String? = null,
  role: Role? = null,
  debounceInterval: Long? = MediumDebounceInterval,
  onClick: () -> Unit,
): Modifier = composed {
  debounceClickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    debounceInterval = debounceInterval,
    onClick = onClick,
  )
}
