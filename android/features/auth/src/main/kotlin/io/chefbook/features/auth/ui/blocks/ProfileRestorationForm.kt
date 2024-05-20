package io.chefbook.features.auth.ui.blocks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ComponentBigHeight
import io.chefbook.features.auth.R
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.libs.utils.time.parseTimestampSafely
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
@SuppressLint("NewApi")
internal fun ProfileRestorationForm(
  state: AuthScreenState.ProfileRestoration,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val deletionTimestamp = remember(state.deletionTimestamp) {
    parseTimestampSafely(state.deletionTimestamp)?.toJavaLocalDateTime()
      ?.atZone(ZoneOffset.systemDefault())
      ?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
      .orEmpty()
  }

  val infoText = buildAnnotatedString {
    append(stringResource(R.string.common_auth_screen_restore_profile_info))
    append("\n")
    append(deletionTimestamp)
    addStyle(
      style = SpanStyle(
        colors.tintPrimary,
        fontWeight = FontWeight.Bold,
      ),
      start = length - deletionTimestamp.length,
      end = length
    )
  }

  Text(
    text = stringResource(R.string.common_auth_screen_profile_deleted),
    style = typography.h3,
    textAlign = TextAlign.Center,
    color = colors.foregroundPrimary,
    modifier = Modifier.fillMaxWidth()
  )
  Spacer(Modifier.height(16.dp))
  Text(
    text = infoText,
    style = typography.body1,
    textAlign = TextAlign.Center,
    color = colors.foregroundPrimary,
    modifier = Modifier.fillMaxWidth()
  )
  Spacer(Modifier.height(24.dp))
  DynamicButton(
    text = stringResource(R.string.common_auth_screen_restore_profile),
    onClick = { onIntent(AuthScreenIntent.RestoreProfile) },
    isEnabled = true,
    isSelected = true,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentBigHeight),
  )
  Spacer(Modifier.height(12.dp))
  Text(
    text = stringResource(R.string.common_auth_screen_sign_out),
    style = typography.headline2,
    textAlign = TextAlign.Center,
    color = colors.tintPrimary,
    modifier = Modifier
      .fillMaxWidth()
      .simpleClickable { onIntent(AuthScreenIntent.OpenSignOutConfirmationScreen) }
      .padding(vertical = 12.dp)
  )
}
