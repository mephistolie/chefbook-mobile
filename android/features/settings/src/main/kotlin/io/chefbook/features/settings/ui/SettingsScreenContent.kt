package io.chefbook.features.settings.ui

import Switch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.colors.Gradients
import io.chefbook.features.settings.BuildConfig
import io.chefbook.features.settings.R
import io.chefbook.features.settings.ui.components.MenuSelectableIcon
import io.chefbook.features.settings.ui.components.MenuSelectableImage
import io.chefbook.features.settings.ui.components.SettingsScreenToolbar
import io.chefbook.features.settings.ui.mvi.SettingsScreenIntent
import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.entities.Environment
import io.chefbook.sdk.settings.api.external.domain.entities.Settings
import io.chefbook.ui.common.components.menu.MenuDivider
import io.chefbook.ui.common.components.menu.MenuGroup
import io.chefbook.ui.common.components.menu.MenuItem
import io.chefbook.ui.common.components.menu.MenuScreen
import io.chefbook.ui.common.components.menu.MenuSection
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun SettingsScreenContent(
  state: Settings,
  onIntent: (SettingsScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  MenuScreen(
    toolbar = {
      SettingsScreenToolbar(
        onBackClick = { onIntent(SettingsScreenIntent.Back) }
      )
    },
  ) {
    MenuGroup(isFirst = true) {
      MenuSection(title = stringResource(R.string.common_settings_screen_theme))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 14.dp),
      ) {
        MenuSelectableIcon(
          icon = ImageVector.vectorResource(designR.drawable.ic_smartphone),
          isSelected = state.appTheme == io.chefbook.sdk.settings.api.external.domain.entities.AppTheme.SYSTEM,
          onClick = { onIntent(SettingsScreenIntent.SetTheme(io.chefbook.sdk.settings.api.external.domain.entities.AppTheme.SYSTEM)) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        MenuSelectableIcon(
          icon = ImageVector.vectorResource(designR.drawable.ic_light_theme),
          isSelected = state.appTheme == io.chefbook.sdk.settings.api.external.domain.entities.AppTheme.LIGHT,
          onClick = { onIntent(SettingsScreenIntent.SetTheme(io.chefbook.sdk.settings.api.external.domain.entities.AppTheme.LIGHT)) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        MenuSelectableIcon(
          icon = ImageVector.vectorResource(designR.drawable.ic_dark_theme),
          isSelected = state.appTheme == io.chefbook.sdk.settings.api.external.domain.entities.AppTheme.DARK,
          onClick = { onIntent(SettingsScreenIntent.SetTheme(io.chefbook.sdk.settings.api.external.domain.entities.AppTheme.DARK)) },
        )
      }
      MenuSection(title = stringResource(R.string.common_settings_screen_icon))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 14.dp),
      ) {
        MenuSelectableImage(
          image = ImageVector.vectorResource(coreR.drawable.ic_broccy),
          isSelected = state.appIcon == AppIcon.STANDARD,
          onClick = { onIntent(SettingsScreenIntent.SetIcon(AppIcon.STANDARD)) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        MenuSelectableImage(
          image = ImageVector.vectorResource(designR.drawable.ic_logo),
          isSelected = state.appIcon == AppIcon.ORIGINAL,
          onClick = { onIntent(SettingsScreenIntent.SetIcon(AppIcon.ORIGINAL)) },
        )
      }
    }
    MenuDivider()
    MenuGroup(isLast = !BuildConfig.DEBUG) {
      MenuItem(
        title = stringResource(R.string.common_settings_screen_open_saved_recipe_expanded),
        onClick = { onIntent(SettingsScreenIntent.SetOpenSavedRecipeExpanded(!state.openSavedRecipeExpanded)) },
        showChevron = false,
        endContent = {
           Switch(isChecked = state.openSavedRecipeExpanded)
        }
      )
    }
    if (BuildConfig.DEBUG) {
      MenuDivider()
      MenuGroup(isLast = true) {
        val isProduction = state.environment == Environment.PRODUCTION
        MenuItem(
          title = stringResource(R.string.common_settings_screen_open_switch_environment),
          onClick = {
            onIntent(SettingsScreenIntent.SetEnvironment(if (isProduction) Environment.DEVELOP else Environment.PRODUCTION))
          },
          showChevron = false,
          endContent = {
            Box(
              modifier = Modifier
                .background(
                  brush = if (isProduction) Gradients.orangeBrush else Gradients.grayBrush(),
                  shape = RoundedCornerShape(100),
                )
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .defaultMinSize(minWidth = 48.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = state.environment.name,
                style = typography.subhead2,
                color = if (isProduction) Color.White else colors.foregroundPrimary,
              )
            }
          }
        )
      }
    }
  }
}
