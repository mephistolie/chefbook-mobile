package io.chefbook.features.recipe.info.ui.components.cooking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.scalingClickable
import io.chefbook.design.components.images.EncryptedImage
import io.chefbook.design.theme.shapes.RoundedCornerShape12

@Composable
internal fun CookingStepPictures(
  pictures: List<String>,
  onPictureClicked: (String) -> Unit,
) {
  for (i in pictures.indices step PICTURE_ROW_CELLS_COUNT) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      for (j in i until i + PICTURE_ROW_CELLS_COUNT) {
        if (j < pictures.size) {
          val pressed = remember { mutableStateOf(false) }
          Box(
            modifier = Modifier
              .weight(1F)
              .aspectRatio(1.25F)
              .height(24.dp)
              .scalingClickable(
                pressed = pressed,
                onClick = { onPictureClicked(pictures[j]) }
              )
              .clip(RoundedCornerShape12)
          ) {
            EncryptedImage(
              data = pictures[j],
              modifier = Modifier.matchParentSize(),
            )
            Shading(isVisible = pressed.value)
          }
        } else {
          Spacer(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1F)
          )
        }
      }
    }
  }
}

private const val PICTURE_ROW_CELLS_COUNT = 3