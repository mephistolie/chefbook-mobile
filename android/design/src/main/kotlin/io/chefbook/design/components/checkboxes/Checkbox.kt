package io.chefbook.design.components.checkboxes

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.ui.checkboxes.CircleCheckbox
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun Checkbox(
  isChecked: Boolean,
  onClick: () -> Unit,
  checkmarkSize: Dp = 20.dp,
  isEnabled: Boolean = true,
) {
  val colors = LocalTheme.colors

  CircleCheckbox(
    isChecked = isChecked,
    onClick = onClick,
    checkedColor = colors.tintPrimary,
    checkmarkColor = colors.tintSecondary,
    checkmarkSize = checkmarkSize,
    enabled = isEnabled,
  )
}
