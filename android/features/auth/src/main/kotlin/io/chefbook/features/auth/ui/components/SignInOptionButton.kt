package io.chefbook.features.auth.form.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.design.components.buttons.CircleIconButton

@Composable
fun SignInOptionButton(
  iconId: Int,
  onClick: () -> Unit,
) {
  CircleIconButton(
    iconId = iconId,
    onClick = onClick,
    modifier = Modifier
      .size(52.dp)
      .padding(5.dp),
  )
}
