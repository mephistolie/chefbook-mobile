package com.mysty.chefbook.features.about.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.dividers.Divider
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.features.about.R
import com.mysty.chefbook.features.about.ui.components.AboutInfo
import com.mysty.chefbook.features.about.ui.components.AboutScreenButton
import com.mysty.chefbook.features.about.ui.components.AboutScreenToolbar
import com.mysty.chefbook.features.about.ui.mvi.AboutScreenIntent

@Composable
internal fun AboutScreenContent(
    versionName: String,
    onIntent: (AboutScreenIntent) -> Unit,
) {
    val colors = LocalTheme.colors

    Column(
        modifier = Modifier
            .background(colors.backgroundPrimary)
            .statusBarsPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AboutScreenToolbar(onBackClick = { onIntent(AboutScreenIntent.Back) })
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { AboutInfo(versionName = versionName) }
            item { Divider(modifier = Modifier.padding(horizontal = 12.dp, bottom = 8.dp)) }
            item { AboutScreenButton(titleId = R.string.common_about_screen_feedback, onClick = {}) }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLightAboutScreen() {
    ThemedAboutScreen(isDarkTheme = false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkAboutScreen() {
    ThemedAboutScreen(isDarkTheme = true)
}

@Composable
private fun ThemedAboutScreen(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = LocalTheme.colors.backgroundPrimary
        ) {
            Column {
                AboutScreenContent(
                    versionName = "Preview",
                    onIntent = {},
                )
            }
        }
    }
}