package com.mysty.chefbook.features.auth.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.design.components.buttons.CircleIconButton
import com.mysty.chefbook.features.auth.R

@Composable
internal fun SignInOptions() {
  Row {
    SignInOptionButton(
      iconId = R.drawable.ic_google,
      onClick = { /*TODO*/ },
    )
    SignInOptionButton(
      iconId = R.drawable.ic_vk,
      onClick = { /*TODO*/ },
    )
    SignInOptionButton(
      iconId = R.drawable.ic_disable_sync,
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