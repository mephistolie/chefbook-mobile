package io.chefbook.features.auth.ui.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.features.auth.ui.mvi.AuthScreenState

@Composable
internal inline fun <reified T : AuthScreenState> AnimatedAuthForm(
  state: AuthScreenState,
  modifier: Modifier = Modifier,
  crossinline content: @Composable ColumnScope.(T) -> Unit,
) {
  AnimatedVisibility(
    visible = state is T,
    enter = slideInHorizontally { fullWidth -> fullWidth },
    exit = slideOutHorizontally { fullWidth -> -fullWidth },
  ) {
    var savedState by remember { mutableStateOf<T?>(null) }

    if (state is T) savedState = state

    val currentState = savedState

    Column(
      modifier = modifier
        .imePadding()
        .padding(horizontal = 48.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      if (currentState != null) {
        content(currentState)
      }
    }
  }
}
