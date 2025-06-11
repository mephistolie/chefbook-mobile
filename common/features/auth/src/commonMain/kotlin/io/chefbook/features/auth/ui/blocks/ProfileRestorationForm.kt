package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.modifiers.clickable.simpleClickable
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ComponentHeight56
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenProfileDeleted
import io.chefbook.features.auth.commonAuthScreenRestoreProfile
import io.chefbook.features.auth.commonAuthScreenRestoreProfileInfo
import io.chefbook.features.auth.commonAuthScreenSignOut
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.libs.utils.time.parseInstantSafely
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileRestorationForm(
  state: AuthScreenState.ProfileRestoration,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val deletionTimestamp = remember<String>(state.deletionTimestamp) {
    val dateTime = parseInstantSafely(state.deletionTimestamp)
      ?.toLocalDateTime(TimeZone.currentSystemDefault()) ?: return@remember ""
    DateTimeComponents.Formats.RFC_1123.format {
      setDateTime(dateTime)
    }
  }

  val infoText = buildAnnotatedString {
    append(stringResource(Res.string.commonAuthScreenRestoreProfileInfo))
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
    text = stringResource(Res.string.commonAuthScreenProfileDeleted),
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
    text = stringResource(Res.string.commonAuthScreenRestoreProfile),
    onClick = { onIntent(AuthScreenIntent.RestoreProfile) },
    enabled = true,
    selected = true,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentHeight56),
  )
  Spacer(Modifier.height(12.dp))
  Text(
    text = stringResource(Res.string.commonAuthScreenSignOut),
    style = typography.headline2,
    textAlign = TextAlign.Center,
    color = colors.tintPrimary,
    modifier = Modifier
      .fillMaxWidth()
      .simpleClickable { onIntent(AuthScreenIntent.OpenSignOutConfirmationScreen) }
      .padding(vertical = 12.dp)
  )
}
