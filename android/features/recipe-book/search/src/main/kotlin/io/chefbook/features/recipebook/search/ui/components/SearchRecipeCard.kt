package io.chefbook.features.recipebook.search.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo
import io.chefbook.ui.common.providers.RecipeEncryptionProvider
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.utils.EmojiUtils
import io.chefbook.core.android.utils.minutesToTimeString
import io.chefbook.design.components.images.EncryptedImage

@Composable
internal fun SearchRecipeCard(
  recipe: DecryptedRecipeInfo,
  modifier: Modifier = Modifier,
  onRecipeClicked: (RecipeInfo) -> Unit,
) {
  val context = LocalContext.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  val placeholder = remember { EmojiUtils.randomFoodEmoji(recipe.id) }

  RecipeEncryptionProvider(
    isEncryptionEnabled = recipe.isEncryptionEnabled,
    isDecrypted = recipe.isDecrypted,
  ) {
    Row(
      modifier = modifier
        .fillMaxWidth()
        .height(48.dp)
        .scalingClickable(
          pressed = pressed,
          debounceInterval = 500L
        ) { onRecipeClicked(recipe) },
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .padding(end = 8.dp)
          .size(48.dp)
          .clippedBackground(colors.backgroundSecondary, RoundedCornerShape(10.dp))
      ) {
        Text(
          text = placeholder,
          style = TextStyle(
            fontSize = TextUnit(24F, TextUnitType.Sp),
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.Center)
            .wrapContentSize()
            .padding(bottom = 2.dp)
            .alpha(0.85F),
        )
        EncryptedImage(recipe.preview)
        Shading(pressed.value)
      }
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(),
      ) {
        Text(
          text = recipe.name,
          style = typography.headline1,
          maxLines = 2,
          color = colors.foregroundPrimary,
        )
        recipe.time?.let {
          Text(
            text = minutesToTimeString(recipe.time, context.resources),
            style = typography.body2,
            color = colors.foregroundSecondary
          )
        }
      }
    }
  }
}
