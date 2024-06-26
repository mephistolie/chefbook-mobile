package io.chefbook.features.recipe.input.ui.dialogs.encryption

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
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.features.recipe.input.R
import io.chefbook.features.recipe.input.ui.dialogs.components.RadioElement
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputDetailsScreenIntent
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun EncryptionStateDialogContent(
  isEncrypted: Boolean,
  onIntent: (RecipeInputDetailsScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .clippedBackground(colors.backgroundPrimary, shape = RoundedCornerShape28Top)
      .padding(horizontal = 18.dp)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Text(
      text = stringResource(coreR.string.common_global_encryption),
      maxLines = 1,
      style = typography.h4,
      color = colors.foregroundPrimary,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 18.dp)
    )
    Divider(
      color = colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    RadioElement(
      icon = ImageVector.vectorResource(designR.drawable.ic_lock_open),
      name = stringResource(coreR.string.common_general_standard),
      description = stringResource(R.string.common_recipe_input_screen_standard_description),
      isSelected = !isEncrypted,
      onSelected = {
        onIntent(RecipeInputDetailsScreenIntent.SetEncryptedState(false))
      },
    )
    RadioElement(
      icon = ImageVector.vectorResource(designR.drawable.ic_lock),
      name = stringResource(coreR.string.common_general_encrypted),
      description = stringResource(R.string.common_recipe_input_screen_encrypted_description),
      isSelected = isEncrypted,
      onSelected = {
        onIntent(RecipeInputDetailsScreenIntent.SetEncryptedState(true))
      },
    )
    Spacer(modifier = Modifier.height(72.dp))
  }
}