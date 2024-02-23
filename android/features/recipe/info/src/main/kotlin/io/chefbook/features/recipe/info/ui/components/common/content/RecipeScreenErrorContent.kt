package io.chefbook.features.recipe.info.ui.components.common.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.BottomSheetCloseButton
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.features.recipe.info.R
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun BoxScope.RecipeScreenErrorContent(
  onReloadClick: () -> Unit,
  onCloseClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .padding(horizontal = 64.dp, vertical = 24.dp)
      .align(Alignment.Center)
      .wrapContentHeight(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
      imageVector = ImageVector.vectorResource(designR.drawable.ic_broccy_grinning_sweat),
      contentDescription = null,
      modifier = Modifier.size(144.dp)
    )
    Text(
      text = stringResource(R.string.common_recipe_screen_recipe_not_found),
      style = typography.h2,
      color = colors.foregroundPrimary
    )
    Divider(
      color = colors.backgroundTertiary,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, bottom = 12.dp)
        .height(1.dp)
    )
    Text(
      text = stringResource(R.string.common_recipe_screen_recipe_not_found_description),
      style = typography.body1,
      modifier = Modifier.align(Alignment.Start),
      color = colors.foregroundSecondary
    )
    Divider(
      color = colors.backgroundTertiary,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .height(1.dp)
    )
    DynamicButton(
      leftIcon = ImageVector.vectorResource(designR.drawable.ic_refresh),
      text = stringResource(coreR.string.common_general_refresh).uppercase(),
      isSelected = true,
      modifier = Modifier
        .fillMaxWidth()
        .height(44.dp),
      onClick = onReloadClick,
    )
  }
  BottomSheetCloseButton(onClick = onCloseClick)
}
