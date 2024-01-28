package io.chefbook.ui.common.components.menu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun MenuSection(
  title: String,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Text(
    text = title,
    modifier = modifier
      .padding(top = 10.dp, bottom = 0.dp)
      .fillMaxSize(),
    textAlign = TextAlign.Start,
    style = typography.h4,
    color = colors.foregroundSecondary,
  )
}
