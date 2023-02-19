package com.mysty.chefbook.features.auth.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.dividers.Divider
import com.mysty.chefbook.features.auth.ui.mvi.AuthAction
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenState

@Composable
internal fun SignInOptionsBlock(
  state: AuthScreenState,
) {
  val colors = LocalTheme.colors

  AnimatedVisibility(visible = state.action == AuthAction.SIGN_IN && !state.isLoading) {
    Column(
      modifier = Modifier.wrapContentHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(Modifier.height(16.dp))
      Divider(
        color = colors.backgroundSecondary,
        modifier = Modifier
          .fillMaxWidth()
          .height(1.dp)
          .padding(96.dp, 0.dp)
      )
      Spacer(Modifier.height(12.dp))
      SignInOptions()
    }
  }
}
