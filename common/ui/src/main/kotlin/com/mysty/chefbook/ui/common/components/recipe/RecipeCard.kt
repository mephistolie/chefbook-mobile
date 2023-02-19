package com.mysty.chefbook.ui.common.components.recipe

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
import androidx.compose.material.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.core.android.utils.EmojiUtils
import com.mysty.chefbook.core.android.utils.minutesToTimeString
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.core.utils.TimeUtils
import com.mysty.chefbook.design.R
import com.mysty.chefbook.design.components.images.EncryptedImage
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.colors.Red
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape12
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape16
import com.mysty.chefbook.ui.common.providers.RecipeEncryptionProvider

@OptIn(ExperimentalUnitApi::class)
@Composable
fun RecipeCard(
  recipe: RecipeInfo,
  modifier: Modifier = Modifier,
  onRecipeClick: (RecipeInfo) -> Unit,
) {
  val context = LocalContext.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  val placeholder = remember { EmojiUtils.randomFoodEmoji(recipe.id) }

  RecipeEncryptionProvider(recipe.encryptionState) {
    Column(
      modifier = modifier
        .fillMaxWidth()
        .scalingClickable(
          pressed = pressed,
          debounceInterval = 500L,
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
        if (recipe.isFavourite) {
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
              painter = painterResource(id = R.drawable.ic_favourite),
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
                .padding(12.dp)
                .size(24.dp),
              contentDescription = "Favourite",
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
        if (recipe.encryptionState !is EncryptionState.Standard) {
          Icon(
            painter = painterResource(id = R.drawable.ic_lock),
            modifier = Modifier.size(12.dp),
            contentDescription = "Encrypted",
            tint = colors.foregroundSecondary
          )
          Spacer(modifier = Modifier.width(4.dp))
        }
        recipe.time?.let { time ->
          Text(
            text = TimeUtils.minutesToTimeString(time, context.resources),
            style = typography.subhead1,
            color = colors.foregroundSecondary
          )
        }
        if (recipe.calories != null) {
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${recipe.calories} ${stringResource(R.string.common_general_kcal)}",
            style = typography.subhead1,
            color = colors.tintPrimary
          )
        }
      }
    }
  }
}

@Composable
@Preview
fun PreviewDetailedRecipeCard() {
  ThemedRecipeCard(
    recipe = RecipeInfo(
      id = Strings.EMPTY,
      name = "Cupcakes",
      calories = 800,
      time = 60,
      isFavourite = true,
      encryptionState = EncryptionState.Encrypted
    ),
    isDarkTheme = false
  )
}

@Composable
@Preview
fun PreviewMinimalRecipeCard() {
  ThemedRecipeCard(
    recipe = RecipeInfo(
      id = Strings.EMPTY,
      name = "Cupcakes",
      calories = 0,
      time = 60,
    ),
    isDarkTheme = true
  )
}

@Composable
private fun ThemedRecipeCard(
  recipe: RecipeInfo,
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      RecipeCard(
        recipe = recipe,
        onRecipeClick = {},
      )
    }
  }
}