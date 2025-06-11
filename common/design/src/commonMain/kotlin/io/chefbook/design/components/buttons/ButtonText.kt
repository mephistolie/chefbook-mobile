package io.chefbook.design.components.buttons

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.internal.SingleLine

@Composable
fun ButtonText(
  text: String?,
  modifier: Modifier = Modifier,
  textStyle: TextStyle = LocalTheme.typography.headline1,
) {
  Text(
    text = text.orEmpty(),
    modifier = modifier,
    style = textStyle,
    color = LocalContentColor.current,
    maxLines = SingleLine,
  )
}
