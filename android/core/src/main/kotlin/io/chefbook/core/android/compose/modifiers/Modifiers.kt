package io.chefbook.core.android.compose.modifiers

import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer

fun Modifier.shimmer(isEnabled: Boolean = true) =
  if (isEnabled) this.shimmer() else this
