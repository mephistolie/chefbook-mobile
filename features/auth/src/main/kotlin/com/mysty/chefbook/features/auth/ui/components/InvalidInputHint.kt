package com.mysty.chefbook.features.auth.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.auth.R
import com.mysty.chefbook.features.auth.ui.mvi.AuthAction
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenState
import com.mysty.chefbook.features.auth.utils.PasswordRating

@Composable
internal fun InvalidInputHint(
  state: AuthScreenState,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val email = state.email
  val password = state.password

  val emailCheck = state.isEmailValid
  val passwordCheck = state.passwordRating

  AnimatedVisibility(
    visible = email.isNotEmpty() && !emailCheck || state.action == AuthAction.SIGN_UP &&
            password.isNotEmpty() && passwordCheck != PasswordRating.VALID
  ) {
    Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_info),
        contentDescription = null,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = if (email.isNotEmpty() && !emailCheck) stringResource(id = R.string.common_auth_screen_invalid_email)
        else {
          when (passwordCheck) {
            PasswordRating.SHORT -> stringResource(id = R.string.common_auth_screen_short_password)
            PasswordRating.UPPER -> stringResource(id = R.string.common_auth_screen_upper_password)
            PasswordRating.LOWER -> stringResource(id = R.string.common_auth_screen_lower_password)
            PasswordRating.NUMBER -> stringResource(id = R.string.common_auth_screen_number_password)
            PasswordRating.SPACE -> stringResource(id = R.string.common_auth_screen_space_password)
            else -> stringResource(id = R.string.common_auth_screen_password_mismatch)
          }
        },
        style = typography.headline1,
        color = colors.foregroundPrimary
      )
    }
  }
}