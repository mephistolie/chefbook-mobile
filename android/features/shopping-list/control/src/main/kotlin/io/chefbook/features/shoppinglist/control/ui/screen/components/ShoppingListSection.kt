package io.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.features.shoppinglist.control.ui.screen.state.ShoppingListSection
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R as designR
import io.chefbook.design.theme.shapes.RoundedCornerShape28

@Composable
internal fun ShoppingListSection(
  state: ShoppingListSection,
  onPurchaseClick: (String) -> Unit,
  onEditPurchaseClick: (String) -> Unit,
  onRecipeOpen: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .clippedBackground(background = colors.backgroundPrimary, RoundedCornerShape28)
      .animateContentSize()
  ) {
    if (!state.title.isNullOrBlank()) {
      Box {
        val pressed = remember { mutableStateOf(false) }
        Row(
          modifier = Modifier
            .scalingClickable(
              scaleFactor = 1F,
              pressed = pressed,
            ) {
              if (state.recipeId != null) onRecipeOpen(state.recipeId)
            }
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = state.title,
            style = typography.h3,
            color = colors.foregroundPrimary,
            modifier = Modifier
              .weight(1F)
              .fillMaxWidth()
          )
          Icon(
            painter = painterResource(id = designR.drawable.ic_arrow_right),
            contentDescription = null,
            tint = colors.foregroundPrimary,
            modifier = Modifier
              .padding(end = 4.dp)
              .size(18.dp)
          )
        }
        Shading(isVisible = pressed.value, color = Color.Black.copy(alpha = 0.1F))
      }
      Divider(
        color = colors.backgroundSecondary,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
          .height(1.dp)
      )
    } else {
      Spacer(modifier = Modifier.height(16.dp))
    }
    state.purchases.forEachIndexed { _, purchase ->
      ShoppingListPurchase(
        purchase = purchase,
        onEditClick = { onEditPurchaseClick(purchase.id) },
        modifier = Modifier
          .simpleClickable { onPurchaseClick(purchase.id) }
          .padding(16.dp, 0.dp, 16.dp, 16.dp),
      )
    }
  }
}