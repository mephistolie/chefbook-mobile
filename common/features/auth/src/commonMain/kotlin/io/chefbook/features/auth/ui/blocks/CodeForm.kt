package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.modifiers.clickable.simpleClickable
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.PinCodeField
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenActivationCode
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CodeForm(
  login: String?,
  code: String,
  codeLength: Int,
  onCodeSet: (String) -> Unit,
  onBackClick: () -> Unit
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val focusRequesters =
    remember { Array(codeLength) { FocusRequester() } }
  val keyboardController = LocalSoftwareKeyboardController.current

  LaunchedEffect(code) {
    if (code.length < AuthScreenState.ProfileActivation.CODE_LENGTH) {
      focusRequesters[code.length].requestFocus()
    }
  }

  AuthFormToolbar(
    login = login,
    onBackClick = onBackClick,
  )
  Spacer(Modifier.height(32.dp))
  Text(
    text = stringResource(Res.string.commonAuthScreenActivationCode),
    style = typography.body2,
    color = colors.foregroundSecondary,
  )
  Spacer(Modifier.height(20.dp))
  Row(
    modifier = Modifier
      .wrapContentWidth()
      .simpleClickable { keyboardController?.show() },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    repeat(AuthScreenState.ProfileActivation.CODE_LENGTH) { index ->
      PinCodeField(
        value = code.getOrNull(index),
        onPinCodeNumAdd = { number -> onCodeSet(code + number) },
        onPinCodeNumRemove = {
          if (code.isNotEmpty()) {
            val newCode = code.substring(0, code.lastIndex)
            onCodeSet(newCode)
          }
        },
        modifier = Modifier
          .focusRequester(focusRequesters[index]),
        isFocused = code.length == index,
      )
    }
  }
}
