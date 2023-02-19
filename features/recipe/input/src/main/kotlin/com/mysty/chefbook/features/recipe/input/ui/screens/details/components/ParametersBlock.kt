package com.mysty.chefbook.features.recipe.input.ui.screens.details.components

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
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.visibility.Visibility
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.theme.dimens.ButtonSmallHeight
import com.mysty.chefbook.features.recipe.input.R
import com.mysty.chefbook.ui.common.extensions.localizedName

@Composable
internal fun ParametersBlock(
  state: RecipeInput,
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
          Visibility.PRIVATE -> stringResource(R.string.common_general_visible_to_you)
          Visibility.SHARED -> stringResource(R.string.common_general_visible_by_link)
          Visibility.PUBLIC -> stringResource(R.string.common_general_visible_to_everyone)
        },
        horizontalPadding = 10.dp,
        cornerRadius = 10.dp,
        textStyle = typography.body2,
        leftIcon = ImageVector.vectorResource(
          when (state.visibility) {
            Visibility.PRIVATE -> R.drawable.ic_eye_closed
            Visibility.SHARED -> R.drawable.ic_link
            Visibility.PUBLIC -> R.drawable.ic_earth
          }
        ),
        iconsSize = 16.dp,
        rightIcon = ImageVector.vectorResource(R.drawable.ic_arrow_down),
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
            .padding(start = 12.dp)
            .height(ButtonSmallHeight),
        onClick = onVisibilityClick,
      )
    }
    item {
      DynamicButton(
        text = "${state.language.flag} ${state.language.localizedName(resources)}",
        horizontalPadding = 10.dp,
        cornerRadius = 10.dp,
        textStyle = typography.body2,
        iconsSize = 16.dp,
        rightIcon = ImageVector.vectorResource(R.drawable.ic_arrow_down),
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier.height(ButtonSmallHeight),
        onClick = onLanguageClick,
      )
    }
    item {
      DynamicButton(
        text = stringResource(if (state.isEncrypted) R.string.common_general_encrypted else R.string.common_general_standard),
        horizontalPadding = 10.dp,
        cornerRadius = 10.dp,
        textStyle = typography.body2,
        iconsSize = 16.dp,
        leftIcon = ImageVector.vectorResource(if (state.isEncrypted) R.drawable.ic_lock else R.drawable.ic_lock_open),
        rightIcon = ImageVector.vectorResource(R.drawable.ic_arrow_down),
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
            .padding(end = 12.dp)
            .height(ButtonSmallHeight),
        onClick = onEncryptionClick,
      )
    }
  }
}
