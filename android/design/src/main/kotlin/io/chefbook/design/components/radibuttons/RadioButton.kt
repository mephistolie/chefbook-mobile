package io.chefbook.design.components.radibuttons

import androidx.compose.runtime.Composable
import com.mephistolie.compost.ui.radiobuttons.OutlineRadioButton
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun RadioButton(
  isSelected: Boolean,
  onClick: () -> Unit,
  isEnabled: Boolean = true,
) {
  OutlineRadioButton(
    isSelected = isSelected,
    onClick = onClick,
    color = LocalTheme.colors.tintPrimary,
    enabled = isEnabled,
  )
}
