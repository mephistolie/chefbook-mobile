package io.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R as designR
import io.chefbook.features.shoppinglist.control.R

internal fun LazyListScope.emptyListBanner(
  modifier: Modifier = Modifier,
) {
  item {
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
        imageVector = ImageVector.vectorResource(designR.drawable.ic_broccy_silence),
        contentDescription = null,
        modifier = Modifier.size(96.dp),
      )
      Spacer(Modifier.height(16.dp))
      Text(
        text = stringResource(R.string.common_shopping_list_screen_empty_here),
        style = typography.h2,
        textAlign = TextAlign.Center,
        color = colors.foregroundPrimary,
      )
    }
  }
}
