package io.chefbook.features.community.recipes.ui.screens.content.components.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape16

@Composable
internal fun FilterButton(
  name: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  foreground: @Composable () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }


  Column(
    modifier = modifier
      .width(68.dp)
      .wrapContentHeight()
      .scalingClickable(pressed, debounceInterval = 1000L) { onClick() },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Box(
      modifier = Modifier
        .size(60.dp)
        .clippedBackground(colors.backgroundSecondary, RoundedCornerShape16),
      contentAlignment = Alignment.Center,
    ) {
      foreground()
    }
    Spacer(Modifier.height(4.dp))
    Text(
      text = name,
      style = typography.caption1,
      textAlign = TextAlign.Center,
      color = colors.foregroundPrimary,
      minLines = 2,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
  }
}
