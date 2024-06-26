package io.chefbook.features.recipebook.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape16
import io.chefbook.sdk.category.api.external.domain.entities.Category

@Composable
fun CategoryCard(
  id: String,
  name: String,
  emoji: String?,
  modifier: Modifier = Modifier,
  onCategoryClicked: (String) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .aspectRatio(0.8f)
      .scalingClickable(
        pressed = pressed,
        debounceInterval = 500L,
      ) {
        onCategoryClicked(id)
      }
      .clippedBackground(colors.backgroundSecondary, RoundedCornerShape(16.dp))
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround,
    ) {
      Text(
        text = name,
        style = typography.subhead2,
        color = colors.foregroundPrimary,
        modifier = Modifier
          .fillMaxSize()
          .weight(1F)
          .padding(12.dp, 8.dp, 12.dp, 0.dp),
        textAlign = TextAlign.Start,
        maxLines = 2
      )
      emoji?.let { cover ->
        Text(
          text = cover,
          style = TextStyle(
            fontSize = TextUnit(56F, TextUnitType.Sp),
            shadow = Shadow(
              color = Color(0x20000000),
              blurRadius = 15F
            )
          ),
          color = colors.foregroundPrimary,
          modifier = Modifier
            .fillMaxSize()
            .weight(2F)
            .clip(RoundedCornerShape16)
            .offset(12.dp, 18.dp),
          textAlign = TextAlign.End,
          maxLines = 1,
          softWrap = false,
        )
      }
    }
    Shading(pressed.value)
  }
}
