package com.mysty.chefbook.features.about.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.toolbar.Toolbar
import com.mysty.chefbook.features.about.R

@Composable
internal fun AboutScreenToolbar(
    onBackClick: () -> Unit
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Toolbar(
        modifier = Modifier.padding(horizontal = 12.dp),
        onLeftButtonClick = onBackClick,
    ) {
        Text(
            text = stringResource(R.string.common_about_screen_title),
            style = typography.h4,
            color = colors.foregroundPrimary,
        )
    }
}
