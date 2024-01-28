package io.chefbook.features.settings.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.toolbar.Toolbar
import io.chefbook.features.settings.R

@Composable
internal fun SettingsScreenToolbar(
    onBackClick: () -> Unit
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Toolbar(
        modifier = Modifier
            .background(colors.backgroundPrimary)
            .statusBarsPadding()
            .padding(horizontal = 12.dp),
        onLeftButtonClick = onBackClick,
    ) {
        Text(
            text = stringResource(R.string.common_settings_screen_title),
            style = typography.h4,
            color = colors.foregroundPrimary,
        )
    }
}
