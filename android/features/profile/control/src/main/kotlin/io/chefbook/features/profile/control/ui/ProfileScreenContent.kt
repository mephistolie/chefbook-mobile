package io.chefbook.features.profile.control.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.colors.Gradients
import io.chefbook.features.profile.control.R
import io.chefbook.features.profile.control.ui.components.ProfileScreenToolbar
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenIntent
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenState
import io.chefbook.sdk.profile.api.external.domain.entities.SubscriptionPlan
import io.chefbook.ui.common.components.menu.MenuDivider
import io.chefbook.ui.common.components.menu.MenuGroup
import io.chefbook.ui.common.components.menu.MenuItem
import io.chefbook.ui.common.components.menu.MenuScreen
import io.chefbook.ui.common.components.profile.ProfileAvatar
import io.chefbook.ui.common.components.profile.SubscriptionBadge
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun ProfileScreenContent(
  state: ProfileScreenState,
  onIntent: (ProfileScreenIntent) -> Unit,
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
        onBack = { onIntent(ProfileScreenIntent.Back) }
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
        title = stringResource(coreR.string.common_general_subscription),
        iconId = designR.drawable.ic_credit_card,
        onClick = {},
        endContent = { SubscriptionBadge(isPremium = state.profile.subscriptionPlan != SubscriptionPlan.FREE) },
      )
      MenuItem(
        title = stringResource(R.string.common_profile_screen_data_exporting),
        subtitle = stringResource(R.string.common_profile_screen_print_recipe_book),
        iconId = designR.drawable.ic_cloud_down,
        onClick = {},
      )
      MenuItem(
        title = stringResource(coreR.string.common_general_profile_editing),
        iconId = designR.drawable.ic_manage_profile,
        onClick = { onIntent(ProfileScreenIntent.OpenProfileEditingScreen) },
      )
      MenuItem(
        title = stringResource(R.string.common_profile_screen_change_profile),
        iconId = designR.drawable.ic_logout,
        onClick = { onIntent(ProfileScreenIntent.RequestLogout) },
      )
    }
    MenuDivider()
    MenuGroup(isLast = true) {
      MenuItem(
        title = stringResource(R.string.common_profile_screen_app_settings),
        iconId = designR.drawable.ic_settings,
        onClick = { onIntent(ProfileScreenIntent.OpenAppSettingsScreen) },
      )
      MenuItem(
        title = stringResource(R.string.common_profile_screen_rate_chefbook),
        iconId = designR.drawable.ic_unliked,
        onClick = { onIntent(ProfileScreenIntent.RateApp) },
      )
      MenuItem(
        title = stringResource(R.string.common_profile_screen_about_app),
        iconId = designR.drawable.ic_info,
        onClick = { onIntent(ProfileScreenIntent.OpenAboutAppScreen) },
      )
    }
  }
}
