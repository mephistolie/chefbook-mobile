package io.chefbook.design.theme

import androidx.compose.runtime.Composable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.compose.providers.theme.ThemeProvider
import io.chefbook.design.theme.colors.palettes.EncryptedDataPalette

@Composable
fun EncryptedDataTheme(
  isEncrypted: Boolean = false,
  content: @Composable () -> Unit
) =
  ThemeProvider(
    colors = if (isEncrypted) EncryptedDataPalette else LocalTheme.colors,
    typography = LocalTheme.typography,
    content = content,
  )
