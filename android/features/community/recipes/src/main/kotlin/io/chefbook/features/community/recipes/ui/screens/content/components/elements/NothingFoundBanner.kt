package io.chefbook.features.community.recipes.ui.screens.content.components.elements

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
import io.chefbook.design.R as designR
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.features.community.recipes.R

@Composable
internal fun NothingFoundBanner(
  offerCreateRecipe: Boolean,
  onCreateRecipeClick: () -> Unit,
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
      imageVector = ImageVector.vectorResource(designR.drawable.ic_broccy_grinning_sweat),
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

    Text(
      text = if (offerCreateRecipe) {
        stringResource(R.string.common_community_recipes_content_screen_create_recipe_hint)
      } else {
        stringResource(R.string.common_community_recipes_content_screen_change_parameters_hint)
      },
      style = typography.body1,
      textAlign = TextAlign.Center,
      color = colors.foregroundSecondary,
    )
    if (offerCreateRecipe) {
      Text(
        text = stringResource(R.string.common_community_recipes_content_screen_public_recipe),
        style = typography.body1,
        textAlign = TextAlign.Center,
        color = colors.tintPrimary,
        modifier = Modifier.simpleClickable(1000L, onCreateRecipeClick)
      )
    }
  }
}
