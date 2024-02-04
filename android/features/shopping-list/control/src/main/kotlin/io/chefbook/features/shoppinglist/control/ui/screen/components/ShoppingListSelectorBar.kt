package io.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.R
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.BottomSheetCloseButton
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.design.R as designR

@Composable
fun ShoppingListSelectorBar(
  title: String?,
  onSelectClick: () -> Unit,
  onCloseClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(shoppingListActionBarHeight)
      .background(colors.backgroundSecondary, RoundedCornerShape28)
      .padding(bottom = 1.dp)
      .clippedBackground(colors.backgroundPrimary, RoundedCornerShape28)
      .padding(horizontal = 8.dp),
  ) {
    Column(
      modifier = Modifier.align(Alignment.Center),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = stringResource(R.string.common_general_shopping_list),
        modifier = Modifier.padding(end = 8.dp),
        maxLines = 1,
        style = typography.subhead1,
        color = colors.foregroundSecondary,
      )
      if (title != null) {
        Row(
          modifier = Modifier.simpleClickable(1000L, onSelectClick),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = title,
            style = typography.h4,
            color = colors.foregroundPrimary,
          )
          Icon(
            imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_down),
            tint = colors.foregroundPrimary,
            modifier = Modifier
              .padding(top = 2.dp)
              .size(16.dp)
              .aspectRatio(1F),
            contentDescription = null,
          )
        }
      }
    }
    BottomSheetCloseButton(
      onClick = onCloseClick,
      modifier = Modifier.align(Alignment.CenterEnd),
      horizontalPadding = 8.dp,
      verticalPadding = 8.dp,
    )
  }
}
