package io.chefbook.features.recipebook.dashboard.ui.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.utils.EmojiUtils
import io.chefbook.core.android.utils.minutesToTimeString
import io.chefbook.design.components.images.EncryptedImage
import io.chefbook.design.theme.shapes.RoundedCornerShape12
import io.chefbook.sdk.recipe.book.api.external.domain.entities.LatestRecipeInfo
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun LatestRecipeCard(
  recipe: LatestRecipeInfo,
  modifier: Modifier = Modifier,
  onRecipeClicked: (String) -> Unit,
) {
  val context = LocalContext.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  val placeholder = remember { EmojiUtils.randomFoodEmoji(recipe.id) }

  Row(
    modifier = modifier
      .padding(end = 12.dp)
      .height(80.dp)
      .aspectRatio(2.25F)
      .scalingClickable(pressed) { onRecipeClicked(recipe.id) }
  ) {
    Box(
      modifier = Modifier
        .padding(end = 8.dp)
        .size(80.dp)
        .clippedBackground(colors.backgroundSecondary, RoundedCornerShape12),
    ) {
      Text(
        text = placeholder,
        style = TextStyle(
          fontSize = TextUnit(28F, TextUnitType.Sp),
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
          .align(Alignment.Center)
          .wrapContentSize()
          .padding(bottom = 4.dp)
          .alpha(0.85F),
      )
      EncryptedImage(recipe.preview)
      Shading(pressed.value)
    }
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column {
        Text(
          text = recipe.name,
          style = typography.subhead1,
          maxLines = 2,
          color = colors.foregroundPrimary,
        )
        recipe.time?.let { time ->
          Text(
            text = minutesToTimeString(time, context.resources),
            style = typography.subhead1,
            color = colors.foregroundSecondary
          )
        }
      }
      Row(
        modifier = Modifier
          .height(28.dp)
          .background(colors.backgroundSecondary, RoundedCornerShape(8.dp))
          .padding(8.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = stringResource(id = coreR.string.common_general_open).uppercase(),
          style = typography.subhead2,
          color = colors.foregroundSecondary,
        )
        Icon(
          imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_right),
          tint = colors.foregroundSecondary,
          modifier = Modifier
            .padding(2.dp, 0.dp, 0.dp, 0.dp)
            .size(8.dp),
          contentDescription = null,
        )
      }
    }
  }
}
