package io.chefbook.core.compose.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.CoroutineScope

@Composable
@NonRestartableComposable
fun LaunchedEffect(
  block: suspend CoroutineScope.() -> Unit
) {
  LaunchedEffect(Unit, block)
}
