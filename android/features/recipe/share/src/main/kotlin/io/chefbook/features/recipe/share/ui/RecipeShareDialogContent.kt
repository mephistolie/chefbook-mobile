package io.chefbook.features.recipe.share.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogIntent
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogState
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.R as coreR
import io.chefbook.design.components.buttons.BottomSheetCloseButton
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.R as designR
import io.chefbook.features.recipe.share.R

@Composable
internal fun RecipeShareDialogContent(
  state: RecipeShareDialogState,
  onIntent: (RecipeShareDialogIntent) -> Unit,
) {
  val context = LocalContext.current

  val typography = LocalTheme.typography
  val colors = LocalTheme.colors

  Column(
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .wrapContentHeight()
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
          color = colors.backgroundPrimary,
          shape = RoundedCornerShape(32.dp, 32.dp, 24.dp, 24.dp)
        ),
      contentAlignment = Alignment.TopEnd
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "${stringResource(coreR.string.common_general_recipe)} #${state.id?.substringBefore('-')}",
          modifier = Modifier.padding(top = 16.dp),
          maxLines = 2,
          style = typography.h2,
          color = colors.foregroundPrimary,
        )
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(state.qr)
            .crossfade(true)
            .build(),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .padding(24.dp, 18.dp, 24.dp)
            .aspectRatio(1F)
            .fillMaxSize()
        )
        Divider(
          color = colors.backgroundSecondary,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(1.dp)
        )
        DynamicButton(
          leftIcon = ImageVector.vectorResource(designR.drawable.ic_link),
          text = stringResource(R.string.common_recipe_share_dialog_copy_link),
          modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(48.dp),
          unselectedForeground = colors.foregroundPrimary,
          onClick = { onIntent(RecipeShareDialogIntent.CopyLink) },
        )
        DynamicButton(
          leftIcon = ImageVector.vectorResource(designR.drawable.ic_message),
          text = stringResource(R.string.common_recipe_share_dialog_send_as_text),
          isSelected = true,
          modifier = Modifier
            .padding(24.dp, 16.dp, 24.dp, 24.dp)
            .fillMaxWidth()
            .height(48.dp),
          onClick = { onIntent(RecipeShareDialogIntent.CopyAsText) },
        )
      }
      BottomSheetCloseButton { onIntent(RecipeShareDialogIntent.Close) }
    }
  }
}
