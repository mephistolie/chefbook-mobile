package io.chefbook.features.recipebook.dashboard.ui.components.blocks.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape16
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun SearchField(
  modifier: Modifier = Modifier,
  onSearchFieldClick: () -> Unit,
  onFavouriteButtonClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Button(
    onClick = onSearchFieldClick,
    modifier = modifier,
    colors = ButtonDefaults.buttonColors(backgroundColor = colors.backgroundSecondary),
    contentPadding = PaddingValues(0.dp),
    shape = RoundedCornerShape16,
    elevation = null,
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        painter = painterResource(id = designR.drawable.ic_search),
        contentDescription = null,
        tint = colors.foregroundPrimary,
        modifier = Modifier.size(18.dp)
      )
      Text(
        text = stringResource(id = coreR.string.common_general_search),
        style = typography.headline1,
        color = colors.foregroundSecondary,
        modifier = Modifier.padding(start = 6.dp),
      )
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .weight(1F))
      Box(
        modifier = Modifier
          .width(2.dp)
          .height(28.dp)
          .background(colors.backgroundTertiary),
      )
      Icon(
        painter = painterResource(id = designR.drawable.ic_favourite),
        contentDescription = null,
        tint = colors.foregroundPrimary,
        modifier = Modifier
          .simpleClickable(1000L, onFavouriteButtonClick)
          .padding(start = 12.dp)
          .size(20.dp),
      )
    }
  }
}