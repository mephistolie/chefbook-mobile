package io.chefbook.features.profile.control.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.features.profile.control.Res
import io.chefbook.features.profile.control.commonProfileScreenAboutApp
import io.chefbook.features.profile.control.commonProfileScreenAppSettings
import io.chefbook.features.profile.control.commonProfileScreenChangeProfile
import io.chefbook.features.profile.control.commonProfileScreenRateChefbook
import io.chefbook.features.profile.control.mvi.ProfileIntent
import io.chefbook.features.profile.control.mvi.ProfileState
import io.chefbook.features.profile.control.ui.components.ProfileScreenToolbar
import io.chefbook.sdk.profile.api.external.domain.entities.SubscriptionPlan
import io.chefbook.ui.common.components.menu.MenuDivider
import io.chefbook.ui.common.components.menu.MenuGroup
import io.chefbook.ui.common.components.menu.MenuItem
import io.chefbook.ui.common.components.menu.MenuScreen
import io.chefbook.ui.common.components.profile.SubscriptionBadge
import io.chefbook.ui.design.components.ProfileAvatar
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.icons.CreditCard
import io.chefbook.ui.design.icons.Info
import io.chefbook.ui.design.icons.Logout
import io.chefbook.ui.design.icons.ManageProfile
import io.chefbook.ui.design.icons.Settings
import io.chefbook.ui.design.icons.Unliked
import io.chefbook.ui.design.theme.colors.Gradients
import io.chefbook.ui.utils.commonGeneralProfileEditing
import io.chefbook.ui.utils.commonGeneralSubscription
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import org.jetbrains.compose.resources.stringResource
import io.chefbook.ui.utils.Res as CoreRes

@Composable
internal fun ProfileScreenContent(
  state: ProfileState,
  onIntent: (ProfileIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val username = if (state.profile.firstName != null || state.profile.lastName != null) {
    (state.profile.firstName?.let { "$it " } + state.profile.lastName).trim()
  } else {
    null
  }
  val email = state.profile.email
  val nickname = state.profile.nickname

  MenuScreen(
    toolbar = {
      ProfileScreenToolbar(
        broccoins = state.profile.broccoins,
        onBack = { onIntent(ProfileIntent.Back) }
      )
    },
  ) {
    MenuGroup(isFirst = true) {
      ProfileAvatar(
        url = state.profile.avatar,
        size = 128.dp,
        strokeBrush = if (state.profile.subscriptionPlan != SubscriptionPlan.FREE) Gradients.orangeBrush else null,
        modifier = Modifier.padding(vertical = 12.dp)
      )
      Text(
        text = username ?: email ?: state.profile.id,
        style = typography.h2,
        color = colors.foregroundPrimary,
      )
      if (username != null && (nickname != null || email != null)) {
        Text(
          text = nickname ?: email.orEmpty(),
          style = typography.caption1,
          color = colors.foregroundSecondary,
        )
      }
      Spacer(modifier = Modifier.height(14.dp))
    }
    MenuDivider()
    MenuGroup {
      MenuItem(
        title = stringResource(CoreRes.string.commonGeneralSubscription),
        icon = ChefBookIcons.CreditCard,
        onClick = {},
        endContent = @Composable { SubscriptionBadge(isPremium = state.profile.subscriptionPlan != SubscriptionPlan.FREE) },
        showChevron = false,
      )
//      MenuItem(
//        title = stringResource(R.string.common_profile_screen_data_exporting),
//        subtitle = stringResource(R.string.common_profile_screen_print_recipe_book),
//        iconId = designR.strings.ic_cloud_down,
//        onClick = {},
//      )
      MenuItem(
        title = stringResource(CoreRes.string.commonGeneralProfileEditing),
        icon = ChefBookIcons.ManageProfile,
        onClick = { onIntent(ProfileIntent.OpenProfileEditingScreen) },
      )
      MenuItem(
        title = stringResource(Res.string.commonProfileScreenChangeProfile),
        icon = ChefBookIcons.Logout,
        onClick = { onIntent(ProfileIntent.RequestLogout) },
      )
    }
    MenuDivider()
    MenuGroup(isLast = true) {
      MenuItem(
        title = stringResource(Res.string.commonProfileScreenAppSettings),
        icon = ChefBookIcons.Settings,
        onClick = { onIntent(ProfileIntent.OpenAppSettingsScreen) },
      )
      MenuItem(
        title = stringResource(Res.string.commonProfileScreenRateChefbook),
        icon = ChefBookIcons.Unliked,
        onClick = { onIntent(ProfileIntent.RateApp) },
      )
      MenuItem(
        title = stringResource(Res.string.commonProfileScreenAboutApp),
        icon = ChefBookIcons.Info,
        onClick = { onIntent(ProfileIntent.OpenAboutAppScreen) },
      )
    }
  }
}
