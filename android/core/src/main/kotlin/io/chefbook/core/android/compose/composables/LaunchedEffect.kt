package io.chefbook.core.android.compose.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.CoroutineScope

@Composable
@NonRestartableComposable
fun LaunchedEffect(
  block: suspend CoroutineScope.() -> Unit
) {
  androidx.compose.runtime.LaunchedEffect(Unit, block)
}
