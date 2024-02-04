package io.chefbook.features.recipe.input.ui.dialogs.visibility

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.ModalBottomSheetShape
import io.chefbook.features.recipe.input.R
import io.chefbook.features.recipe.input.ui.dialogs.components.RadioElement
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputDetailsScreenIntent
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta.Visibility
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun VisibilityDialogContent(
  selectedVisibility: Visibility,
  onIntent: (RecipeInputDetailsScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .clippedBackground(colors.backgroundPrimary, shape = ModalBottomSheetShape)
      .padding(horizontal = 18.dp)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Text(
      text = stringResource(coreR.string.common_general_visibility),
      maxLines = 1,
      style = typography.h4,
      color = colors.foregroundPrimary,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 18.dp)
    )
    Divider(
      color = colors.backgroundTertiary,
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    RadioElement(
      icon = ImageVector.vectorResource(designR.drawable.ic_eye_closed),
      name = stringResource(coreR.string.common_general_only_author),
      description = stringResource(R.string.common_recipe_input_screen_private_description),
      isSelected = selectedVisibility == Visibility.PRIVATE,
      onSelected = {
        onIntent(RecipeInputDetailsScreenIntent.SetVisibility(Visibility.PRIVATE))
      },
    )
    RadioElement(
      icon = ImageVector.vectorResource(designR.drawable.ic_link),
      name = stringResource(coreR.string.common_general_by_link),
      description = stringResource(R.string.common_recipe_input_screen_shared_description),
      isSelected = selectedVisibility == Visibility.LINK,
      onSelected = {
        onIntent(RecipeInputDetailsScreenIntent.SetVisibility(Visibility.LINK))
      },
    )
    RadioElement(
      icon = ImageVector.vectorResource(designR.drawable.ic_earth),
      name = stringResource(coreR.string.common_general_community),
      description = stringResource(R.string.common_recipe_input_screen_public_description),
      isSelected = selectedVisibility == Visibility.PUBLIC,
      onSelected = {
        onIntent(RecipeInputDetailsScreenIntent.SetVisibility(Visibility.PUBLIC))
      },
    )
    Spacer(modifier = Modifier.height(72.dp))
  }
}
