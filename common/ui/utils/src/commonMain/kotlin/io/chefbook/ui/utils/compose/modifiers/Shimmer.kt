package io.chefbook.ui.utils.compose.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer

@Composable
fun Modifier.shimmer(isEnabled: Boolean = true): Modifier =
  if (isEnabled) this.shimmer() else this
