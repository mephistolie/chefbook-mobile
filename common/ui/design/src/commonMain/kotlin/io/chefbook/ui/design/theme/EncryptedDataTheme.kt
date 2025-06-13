package io.chefbook.ui.design.theme

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.utils.compose.providers.theme.ThemeProvider
import io.chefbook.ui.design.theme.colors.palettes.EncryptedDataPalette

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
