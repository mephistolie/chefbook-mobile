package io.chefbook.features.profile.editing.ui.components

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

@Composable
fun DescriptionInputBlock(
  description: String,
  onDescriptionChange: (String) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Text(
    text = stringResource(R.string.common_profile_editing_screen_about_you),
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .fillMaxWidth(),
    style = typography.h3,
    color = colors.foregroundPrimary,
  )
  Spacer(modifier = Modifier.height(8.dp))
  OutlinedTextField(
    value = description,
    onValueChange = onDescriptionChange,
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .fillMaxWidth(),
    minLines = 2,
    maxLines = Int.MAX_VALUE,
    hint = stringResource(R.string.common_profile_editing_screen_about_you_hint),
  )
}