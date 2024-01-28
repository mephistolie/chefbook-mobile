package io.chefbook.features.recipebook.dashboard.ui.components.blocks.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.dividers.Divider
import io.chefbook.design.R as designR

val bottomBarHeight = 69.dp

@Composable
internal fun BottomBar(
  modifier: Modifier = Modifier,
  isScrollUpButtonVisible: Boolean = false,
  onScrollUpButtonClick: () -> Unit,
  onShoppingListIconClick: () -> Unit,
  onSearchFieldClick: () -> Unit,
  onFavouriteButtonClick: () -> Unit,
) {
  val colors = LocalTheme.colors

  Column(
    modifier = modifier
      .background(colors.backgroundPrimary)
      .systemBarsPadding()
      .height(bottomBarHeight)
  ) {
    Divider(color = colors.backgroundSecondary)
    Row(
      modifier = Modifier
        .fillMaxHeight()
        .padding(12.dp, 12.dp, 12.dp, 8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        painter = painterResource(id = designR.drawable.ic_list),
        tint = colors.foregroundPrimary,
        contentDescription = null,
        modifier = Modifier
          .simpleClickable(1000L, onClick = onShoppingListIconClick)
          .padding(end = 8.dp)
          .size(28.dp)
      )
      SearchField(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F),
        onSearchFieldClick = onSearchFieldClick,
        onFavouriteButtonClick = onFavouriteButtonClick,
      )
      AnimatedVisibility(isScrollUpButtonVisible) {
        Icon(
          painter = painterResource(id = designR.drawable.ic_arrow_up),
          tint = colors.foregroundPrimary,
          contentDescription = null,
          modifier = Modifier
            .simpleClickable(1000L, onClick = onScrollUpButtonClick)
            .padding(start = 8.dp)
            .size(28.dp)
        )
      }
    }
  }
}