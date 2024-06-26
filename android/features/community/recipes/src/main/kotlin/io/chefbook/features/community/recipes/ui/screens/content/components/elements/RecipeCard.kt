package io.chefbook.features.community.recipes.ui.screens.content.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import io.chefbook.design.R
import io.chefbook.design.components.images.EncryptedImage
import io.chefbook.design.theme.colors.Red
import io.chefbook.design.theme.shapes.RoundedCornerShape12
import io.chefbook.design.theme.shapes.RoundedCornerShape16
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo

@Composable
fun RecipeCard(
  recipe: DecryptedRecipeInfo,
  modifier: Modifier = Modifier,
  onRecipeClick: (RecipeInfo) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val context = LocalContext.current

  val pressed = remember { mutableStateOf(false) }

  val placeholder = remember {
    recipe.categories.firstOrNull { it.emoji != null }?.emoji ?: EmojiUtils.randomFoodEmoji(recipe.id)
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .scalingClickable(
        pressed = pressed,
        debounceInterval = 1000L,
      ) { onRecipeClick(recipe) }
  ) {
    Box(
      contentAlignment = Alignment.TopEnd,
      modifier = Modifier
        .padding(bottom = 8.dp)
        .aspectRatio(1F)
        .clippedBackground(colors.backgroundSecondary, RoundedCornerShape16)
    ) {
      Text(
        text = placeholder,
        style = TextStyle(
          fontSize = TextUnit(56F, TextUnitType.Sp),
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
          .align(Alignment.Center)
          .wrapContentSize()
          .padding(bottom = 8.dp)
          .alpha(0.85F),
      )
      EncryptedImage(
        data = recipe.preview,
        modifier = Modifier
          .fillMaxSize(),
      )
      if (recipe.isFavourite || recipe.isSaved) {
        Box(
          modifier = Modifier
            .size(64.dp)
            .background(
              brush = Brush.linearGradient(
                colors = listOf(
                  Color(0x00000000),
                  Color(0x00000000),
                  Color(0x1A000000)
                ),
                start = Offset(0F, Float.POSITIVE_INFINITY),
                end = Offset(Float.POSITIVE_INFINITY, 0f)
              ),
              shape = RoundedCornerShape12
            ),
          contentAlignment = Alignment.TopEnd
        ) {
          Icon(
            painter = when {
              recipe.isFavourite -> painterResource(id = R.drawable.ic_favourite)
              else -> painterResource(id = R.drawable.ic_bookmark_fill)
            },
            modifier = Modifier
              .background(
                brush = Brush.radialGradient(
                  colors = listOf(
                    Red.copy(alpha = 0.4F),
                    Red.copy(alpha = 0.05F),
                    Color.Transparent,
                  ),
                ),
              )
              .padding(
                top = 12.dp,
                end = if (recipe.isFavourite) 12.dp else 10.dp)
              .size(24.dp),
            contentDescription = null,
            tint = Red
          )
        }
      }
      Shading(pressed.value)
    }
    Text(
      text = recipe.name,
      style = typography.body1,
      maxLines = 2,
      color = colors.foregroundPrimary,
    )
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_star),
        modifier = Modifier.size(12.dp),
        contentDescription = null,
        tint = colors.tintPrimary
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = if (recipe.rating.votes > 0) recipe.rating.index.toString() else "–",
        style = typography.subhead1,
        color = colors.foregroundSecondary
      )
      recipe.time?.let { time ->
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = minutesToTimeString(time, context.resources),
          style = typography.subhead1,
          color = colors.foregroundSecondary
        )
      }
    }
  }
}
