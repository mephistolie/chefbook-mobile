package io.chefbook.features.auth.form.ui.blocks

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import io.chefbook.design.R
import io.chefbook.features.auth.form.ui.components.SignInOptionButton

@Composable
internal fun SignInOptionsBlock(
  onSignInGoogleClick: () -> Unit,
) {
  Row {
    SignInOptionButton(
      iconId = R.drawable.ic_google,
      onClick = onSignInGoogleClick,
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
