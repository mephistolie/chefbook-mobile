package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import io.chefbook.design.icons.ChefBookIcons
import io.chefbook.design.icons.Google
import io.chefbook.design.icons.SyncOff
import io.chefbook.design.icons.VK
import io.chefbook.features.auth.ui.components.SignInOptionButton

@Composable
internal fun SignInOptionsBlock(
  onSignInGoogleClick: () -> Unit,
) {
  Row {
    SignInOptionButton(
      icon = ChefBookIcons.Google,
      onClick = onSignInGoogleClick,
    )
    SignInOptionButton(
      icon = ChefBookIcons.VK,
      onClick = { /*TODO*/ },
    )
    SignInOptionButton(
      icon = ChefBookIcons.SyncOff,
      onClick = { /*TODO*/ },
    )
  }
}
