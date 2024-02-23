package io.chefbook.features.recipebook.search.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.R as coreR
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.features.recipebook.search.R

@Composable
internal fun NothingFoundBanner(
  showCommunitySearchHint: Boolean,
  onCommunitySearchClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = modifier
      .padding(all = 48.dp)
      .fillMaxSize()
      .wrapContentHeight(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Image(
      imageVector = ImageVector.vectorResource(coreR.drawable.ic_broccy_grinning_sweat),
      contentDescription = null,
      modifier = Modifier.size(96.dp),
    )
    Spacer(Modifier.height(32.dp))
    Text(
      text = stringResource(coreR.string.common_general_nothing_found),
      style = typography.h2,
      textAlign = TextAlign.Center,
      color = colors.foregroundPrimary,
    )
    Spacer(Modifier.height(16.dp))

    if (showCommunitySearchHint) {
      Text(
        text = stringResource(R.string.common_recipe_book_search_screen_search_in_community),
        style = typography.body1,
        textAlign = TextAlign.Center,
        color = colors.tintPrimary,
        modifier = Modifier.simpleClickable(1000L, onCommunitySearchClick)
      )
    }
  }
}
