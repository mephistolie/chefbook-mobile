package io.chefbook.features.about.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.chefbook.ui.common.components.menu.MenuDivider
import io.chefbook.ui.common.components.menu.MenuGroup
import io.chefbook.ui.common.components.menu.MenuItem
import io.chefbook.ui.common.components.menu.MenuScreen
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.features.about.R
import io.chefbook.features.about.ui.components.AboutInfo
import io.chefbook.features.about.ui.components.AboutScreenToolbar
import io.chefbook.features.about.ui.mvi.AboutScreenIntent

@Composable
internal fun AboutScreenContent(
  versionName: String,
  versionCode: Long,
  buildType: String,
  onIntent: (AboutScreenIntent) -> Unit,
) {
  MenuScreen(
    toolbar = {
      AboutScreenToolbar(
        onBackClick = { onIntent(AboutScreenIntent.Back) }
      )
    },
  ) {
    MenuGroup(isFirst = true) {
      AboutInfo(versionName = versionName, versionCode = versionCode, buildType = buildType)
    }
    MenuDivider()
    MenuGroup(isLast = true) {
      MenuItem(
        title = stringResource(R.string.common_about_screen_feedback),
        onClick = {}
      )
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
          versionCode = 1,
          buildType = "DEBUG",
          onIntent = {},
        )
      }
    }
  }
}