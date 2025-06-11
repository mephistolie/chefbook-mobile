package io.chefbook.design.components.textfields.internal

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import io.chefbook.core.compose.constants.ShortDebounceInterval
import io.chefbook.core.compose.modifiers.clickable.simpleClickable
import io.chefbook.core.compose.providers.theme.LocalTheme

@Composable
fun TextFieldIcon(
  imageVector: ImageVector,
  modifier: Modifier = Modifier,
  contentDescription: @Composable () -> String? = { null },
  tint: Color = LocalTheme.colors.tintPrimary,
  onClickLabel: @Composable () -> String? = { null },
  onClick: (() -> Unit)? = null,
) = Icon(
  imageVector = imageVector,
  contentDescription = contentDescription(),
  modifier = modifier
    .run {
      if (onClick == null) return@run this

      simpleClickable(
        role = Role.Button,
        debounceInterval = ShortDebounceInterval,
        onClickLabel = onClickLabel(),
        onClick = onClick,
      )
    },
  tint = tint,
)
