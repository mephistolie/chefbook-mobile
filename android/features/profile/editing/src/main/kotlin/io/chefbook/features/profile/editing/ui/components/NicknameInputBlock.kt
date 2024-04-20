package io.chefbook.features.profile.editing.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.OutlinedTextField
import io.chefbook.features.profile.editing.R

private const val PROFILE_URL_PREFIX = "https://chefbook.io/profiles"

@Composable
fun NicknameInputBlock(
  nickname: String,
  onNicknameChange: (String) -> Unit,
  isValid: Boolean = true,
  hint: String? = null,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Text(
    text = stringResource(R.string.common_profile_editing_screen_nickname),
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .fillMaxWidth(),
    style = typography.h3,
    color = colors.foregroundPrimary,
  )
  Spacer(modifier = Modifier.height(8.dp))
  OutlinedTextField(
    value = nickname,
    onValueChange = onNicknameChange,
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .fillMaxWidth(),
    isError = !isValid,
    hint = stringResource(R.string.common_profile_editing_screen_nickname),
  )
  AnimatedVisibility(
    visible = hint != null,
    enter = fadeIn() + expandVertically(),
    exit = fadeOut() + shrinkVertically(),
  ) {
    Column {
      Text(
        text = hint.orEmpty(),
        modifier = Modifier
          .padding(16.dp, 8.dp, 16.dp)
          .fillMaxWidth(),
        style = typography.body2,
        color = colors.foregroundSecondary,
      )
      if (isValid && nickname.isNotBlank()) {
        Text(
          text = "$PROFILE_URL_PREFIX/$nickname",
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
          style = typography.body2,
          color = colors.tintPrimary,
        )
      }
    }
  }
}