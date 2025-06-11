package io.chefbook.features.recipe.input.ui.screens.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.icons.ArrowDownMedium
import io.chefbook.design.icons.ChefBookIcons
import io.chefbook.design.theme.dimens.ComponentHeight40
import io.chefbook.libs.models.visibility.Visibility
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.core.res as CoreR
import io.chefbook.design.R as designR

@Composable
internal fun ParametersBlock(
  state: RecipeInput,
  isEditing: Boolean,
  onVisibilityClick: () -> Unit,
  onLanguageClick: () -> Unit,
  onEncryptionClick: () -> Unit,
) {
  val resources = LocalContext.current.resources

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  LazyRow(
    modifier = Modifier.padding(top = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    item {
      DynamicButton(
        text = when (state.visibility) {
          Visibility.PRIVATE -> stringResource(CoreR.string.common_general_visible_to_you)
          Visibility.LINK -> stringResource(CoreR.string.common_general_visible_by_link)
          Visibility.PUBLIC -> stringResource(CoreR.string.common_general_visible_to_everyone)
        },
        horizontalPadding = 10.dp,
        cornerRadius = 12.dp,
        textStyle = typography.body2,
        leftIcon = ImageVector.vectorResource(
          when (state.visibility) {
            Visibility.PRIVATE -> designR.drawable.ic_eye_closed
            Visibility.LINK -> designR.drawable.ic_link
            Visibility.PUBLIC -> designR.drawable.ic_earth
          }
        ),
        iconsSize = 16.dp,
        rightIcon = ChefBookIcons.ArrowDownMedium,
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
          .padding(start = 12.dp)
          .height(ComponentHeight40),
        onClick = onVisibilityClick,
      )
    }
    item {
      DynamicButton(
        text = "${state.language.flag} ${state.language.localizedName(resources)}",
        horizontalPadding = 10.dp,
        cornerRadius = 12.dp,
        textStyle = typography.body2,
        iconsSize = 16.dp,
        rightIcon = ChefBookIcons.ArrowDownMedium,
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier.height(ComponentHeight40),
        onClick = onLanguageClick,
      )
    }
    item {
      DynamicButton(
        text = stringResource(if (state.hasEncryption) CoreR.string.common_general_encrypted else CoreR.string.common_general_standard),
        horizontalPadding = 10.dp,
        cornerRadius = 12.dp,
        textStyle = typography.body2,
        iconsSize = 16.dp,
        isEnabled = !isEditing,
        leftIcon = ImageVector.vectorResource(if (state.hasEncryption) designR.drawable.ic_lock else designR.drawable.ic_lock_open),
        rightIcon = if (!isEditing) ChefBookIcons.ArrowDownMedium else null,
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
          .padding(end = 12.dp)
          .height(ComponentHeight40),
        onClick = onEncryptionClick,
      )
    }
  }
}
