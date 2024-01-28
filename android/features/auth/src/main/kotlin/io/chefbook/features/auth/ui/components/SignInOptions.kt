package io.chefbook.features.auth.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.design.components.buttons.CircleIconButton
import io.chefbook.design.R as designR

@Composable
internal fun SignInOptions() {
  Row {
    SignInOptionButton(
      iconId = designR.drawable.ic_google,
      onClick = { /*TODO*/ },
    )
    SignInOptionButton(
      iconId = designR.drawable.ic_vk,
      onClick = { /*TODO*/ },
    )
    SignInOptionButton(
      iconId = designR.drawable.ic_disable_sync,
      onClick = { /*TODO*/ },
    )
  }
}

@Composable
private fun SignInOptionButton(
  iconId: Int,
  onClick: () -> Unit,
) {
  CircleIconButton(
    iconId = iconId,
    onClick = onClick,
    modifier = Modifier
      .size(48.dp)
      .padding(5.dp),
  )
}