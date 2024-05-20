package io.chefbook.features.about.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.features.about.R
import io.chefbook.features.about.data.EULA_URL
import io.chefbook.features.about.data.SUPPORT_EMAIL
import io.chefbook.features.about.data.TELEGRAM_CHANNEL_URL
import io.chefbook.features.about.data.VK_GROUP_URL
import io.chefbook.features.about.ui.components.AboutInfo
import io.chefbook.features.about.ui.components.AboutScreenToolbar
import io.chefbook.features.about.ui.mvi.AboutScreenIntent
import io.chefbook.ui.common.components.menu.MenuDivider
import io.chefbook.ui.common.components.menu.MenuGroup
import io.chefbook.ui.common.components.menu.MenuItem
import io.chefbook.ui.common.components.menu.MenuScreen
import io.chefbook.core.android.R as coreR

@Composable
internal fun AboutScreenContent(
  versionName: String,
  versionCode: Long,
  buildType: String,
  onIntent: (AboutScreenIntent) -> Unit,
) {
  val context = LocalContext.current

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
    MenuGroup {
      MenuItem(
        title = stringResource(R.string.common_about_telegram),
        onClick = { openUrl(context, TELEGRAM_CHANNEL_URL) }
      )
      MenuItem(
        title = stringResource(R.string.common_about_vk),
        onClick = { openUrl(context, VK_GROUP_URL) }
      )
      MenuItem(
        title = stringResource(R.string.common_about_screen_feedback),
        onClick = { openEmailSending(context) }
      )
    }
    MenuDivider()
    MenuGroup(isLast = true) {
      MenuItem(
        title = stringResource(coreR.string.common_general_eula),
        onClick = { openUrl(context, EULA_URL) }
      )
    }
  }
}

private fun openUrl(context: Context, url: String) {
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
  context.startActivity(intent)
}

private fun openEmailSending(context: Context) {
  val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$SUPPORT_EMAIL"))
  context.startActivity(intent)
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