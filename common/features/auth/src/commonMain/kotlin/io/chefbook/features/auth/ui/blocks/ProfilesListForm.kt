package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.ui.utils.compose.modifiers.clickable.simpleClickable
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.components.ProfileAvatar
import io.chefbook.ui.design.components.buttons.DynamicButton
import io.chefbook.ui.design.components.buttons.selectableButtonColors
import io.chefbook.ui.design.icons.ArrowEndMedium
import io.chefbook.ui.design.icons.ArrowStart
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.theme.dimens.ComponentHeight56
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenAnotherAccount
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.libs.models.profile.ProfileInfo
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfilesListForm(
  state: AuthScreenState.ProfileList,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    items(
      items = state.profiles,
      key = ProfileInfo::id,
    ) { profile ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(ComponentHeight56)
          .simpleClickable { onIntent(AuthScreenIntent.SignInProfile(profile.id)) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight(),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          ProfileAvatar(
            url = profile.avatar,
            size = 44.dp,
          )
          Spacer(modifier = Modifier.width(16.dp))
          Text(
            text = profile.name.orEmpty(),
            style = typography.h4,
            color = colors.foregroundPrimary,
          )
        }

        Icon(
          imageVector = ChefBookIcons.ArrowEndMedium,
          tint = colors.foregroundPrimary,
          modifier = Modifier.size(24.dp),
          contentDescription = null,
        )
      }
    }
  }
  Spacer(Modifier.height(32.dp))
  Row {
    DynamicButton(
      text = stringResource(Res.string.commonAuthScreenAnotherAccount),
      onClick = { onIntent(AuthScreenIntent.OpenSignInForm) },
      textStyle = typography.headline1,
      colors = selectableButtonColors(
        unselectedContentColor = colors.foregroundPrimary,
      ),
      modifier = Modifier
        .weight(1F)
        .fillMaxWidth()
        .height(ComponentHeight56),
    )
    Spacer(modifier = Modifier.width(8.dp))
    DynamicButton(
      leadIcon = ChefBookIcons.ArrowStart,
//      leftIcon = ImageVector.vectorResource(io.chefbook.ui.design.R.drawable.ic_manage_profile),
      onClick = { onIntent(AuthScreenIntent.OpenSignInForm) },
      textStyle = typography.headline1,
      colors = selectableButtonColors(
        unselectedContentColor = colors.foregroundPrimary,
      ),
      modifier = Modifier
        .size(ComponentHeight56),
    )
  }

}
