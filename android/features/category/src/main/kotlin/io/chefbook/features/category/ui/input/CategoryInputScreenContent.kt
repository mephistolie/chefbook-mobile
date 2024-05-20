package io.chefbook.features.category.ui.input

import androidx.compose.animation.animateColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.utils.EmojiUtils
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.components.buttons.LoadingButton
import io.chefbook.design.components.spacers.VerticalSpacer
import io.chefbook.design.components.textfields.OutlinedTextField
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.dimens.ComponentMediumHeight
import io.chefbook.design.theme.dimens.DefaultIconSize
import io.chefbook.features.category.R
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenIntent
import io.chefbook.features.category.ui.input.mvi.CategoryInputScreenState
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.ui.common.dialogs.StandardDialog
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@Composable
internal fun CategoryInputScreenContent(
  state: CategoryInputScreenState,
  onIntent: (CategoryInputScreenIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val isEditing = state.isEditing
  val isProcessing = state.isSaving || state.isDeleting
  val name = state.input.name
  val cover = state.input.emoji.orEmpty()

  val colors = LocalTheme.colors

  val coverPlaceholder = remember { EmojiUtils.randomFoodEmoji() }

  StandardDialog(
    title = stringResource(
      if (isEditing) {
        R.string.common_category_input_dialog_edit_category
      } else {
        R.string.common_category_input_dialog_new_category
      }
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      OutlinedTextField(
        value = cover,
        onValueChange = { text -> onIntent(CategoryInputScreenIntent.SetCover(text)) },
        modifier = modifier.width(56.dp),
        textOpacity = if (cover.isNotEmpty()) 1F else 0.5F,
        hint = coverPlaceholder,
        readOnly = isProcessing,
      )
      OutlinedTextField(
        value = name,
        onValueChange = { text -> onIntent(CategoryInputScreenIntent.SetName(text)) },
        modifier = modifier.fillMaxWidth(),
        hint = stringResource(coreR.string.common_general_name),
        readOnly = isProcessing,
      )
    }
    VerticalSpacer(24.dp)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      if (isEditing) {
        LoadingButton(
          onClick = { onIntent(CategoryInputScreenIntent.Delete) },
          isLoading = state.isDeleting,
          unselectedForeground = colors.foregroundPrimary,
          modifier = Modifier
            .padding(end = 4.dp)
            .width(56.dp)
            .height(ComponentMediumHeight),
        ) {
          Icon(
            imageVector = ImageVector.vectorResource(designR.drawable.ic_trash),
            tint = colors.foregroundPrimary,
            modifier = Modifier
              .size(DefaultIconSize)
              .aspectRatio(1F),
            contentDescription = null,
          )
        }
      }

      val buttonsModifier = Modifier
        .weight(1F)
        .fillMaxWidth()
        .height(ComponentMediumHeight)
      DynamicButton(
        onClick = { onIntent(CategoryInputScreenIntent.Cancel) },
        modifier = buttonsModifier,
        leftIcon = ImageVector.vectorResource(designR.drawable.ic_cross),
        selectedForeground = colors.foregroundPrimary,
        selectedBackground = colors.backgroundSecondary,
        isSelected = !isProcessing,
        isEnabled = !isProcessing,
        disableScaling = true,
      )
      LoadingButton(
        onClick = { onIntent(CategoryInputScreenIntent.ConfirmInput) },
        isLoading = state.isSaving,
        modifier = buttonsModifier,
        isSelected = name.isNotEmpty() && !state.isDeleting,
        isEnabled = !state.isDeleting,
        disableScaling = true,
      ) { transition ->
        val foreground by transition.animateColor(label = "foreground") { selected ->
          if (selected) Color.White else colors.foregroundSecondary
        }

        Icon(
          imageVector = ImageVector.vectorResource(designR.drawable.ic_check),
          tint = foreground,
          modifier = Modifier
            .size(DefaultIconSize)
            .aspectRatio(1F),
          contentDescription = null,
        )
      }
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightCategoryInputDialog() {
  ThemedCategoryInputDialog(isEditing = false, isDarkTheme = false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkCategoryInputDialog() {
  ThemedCategoryInputDialog(isEditing = true, isDarkTheme = true)
}

@Composable
private fun ThemedCategoryInputDialog(
  isEditing: Boolean,
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    CategoryInputScreenContent(
      state = CategoryInputScreenState(
        input = CategoryInput(
          name = if (isEditing) "Cupcake" else "",
        ),
        isEditing = isEditing,
      ),
      onIntent = {},
    )
  }
}
