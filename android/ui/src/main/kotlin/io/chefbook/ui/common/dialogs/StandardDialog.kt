package io.chefbook.ui.common.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.spacers.VerticalSpacer
import io.chefbook.design.theme.shapes.DialogShape

@Composable
fun StandardDialog(
  modifier: Modifier = Modifier,
  title: String? = null,
  description: String? = null,
  content: @Composable ColumnScope.() -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(12.dp)
      .background(
        color = colors.backgroundPrimary,
        shape = DialogShape,
      )
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    title?.let {
      Text(
        text = title,
        style = typography.h3,
        color = colors.foregroundPrimary
      )
    }
    if (title != null && description != null) VerticalSpacer(12.dp)
    description?.let {
      Text(
        text = description,
        style = typography.body1,
        color = colors.foregroundSecondary,
        textAlign = TextAlign.Center,
      )
    }
    VerticalSpacer(32.dp)
    content()
  }
}