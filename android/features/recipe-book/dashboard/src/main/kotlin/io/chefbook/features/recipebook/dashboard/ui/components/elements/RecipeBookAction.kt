package io.chefbook.features.recipebook.dashboard.ui.components.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape12
import io.chefbook.design.theme.shapes.RoundedCornerShape16
import io.chefbook.design.R as designR

@Composable
fun RecipeBookActionButton(
  modifier: Modifier = Modifier,
  imageModifier: Modifier = Modifier,
  title: String? = null,
  subtitle: String? = null,
  hint: String? = null,
  image: Painter? = null,
  onActionButtonClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .scalingClickable(pressed, onClick = onActionButtonClick)
      .clippedBackground(colors.backgroundSecondary, RoundedCornerShape16)
  ) {
    image?.let {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
      ) {
        Image(
          painter = image,
          modifier = imageModifier,
          contentDescription = null,
        )
      }
    }
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp, 10.dp, 50.dp, 10.dp),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        title?.let {
          Text(
            text = title,
            style = typography.subhead1,
            color = colors.foregroundPrimary
          )
        }
        subtitle?.let {
          Text(
            text = subtitle,
            style = typography.caption1,
            color = colors.foregroundSecondary
          )
        }
      }
      hint?.let {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = hint,
            style = typography.subhead2,
            color = colors.foregroundSecondary
          )
          Icon(
            imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_right),
            tint = colors.foregroundSecondary,
            modifier = Modifier
              .padding(2.dp, 2.dp, 0.dp, 0.dp)
              .size(8.dp),
            contentDescription = null,
          )
        }
      }
    }
    Shading(pressed.value)
  }
}
