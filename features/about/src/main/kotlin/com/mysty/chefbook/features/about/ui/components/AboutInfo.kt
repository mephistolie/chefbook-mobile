package com.mysty.chefbook.features.about.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.about.R

@Composable
internal fun AboutInfo(
    versionName: String,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_broccy),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 40.dp)
                .size(128.dp),
        )
        Text(
            text = stringResource(R.string.common_about_screen_version, versionName),
            style = typography.body2,
            color = colors.foregroundSecondary,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(R.string.common_about_screen_made_by),
            style = typography.headline2,
            color = colors.foregroundSecondary,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = stringResource(R.string.common_about_screen_dedicated_to),
            style = typography.headline2,
            color = colors.foregroundSecondary,
            modifier = Modifier.padding(bottom = 36.dp)
        )
    }
}
