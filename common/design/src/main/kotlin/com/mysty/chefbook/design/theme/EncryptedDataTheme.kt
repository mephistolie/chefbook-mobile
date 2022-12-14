package com.mysty.chefbook.design.theme

import androidx.compose.runtime.Composable
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.core.ui.compose.providers.theme.ThemeProvider
import com.mysty.chefbook.design.theme.colors.palettes.EncryptedDataPalette

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
