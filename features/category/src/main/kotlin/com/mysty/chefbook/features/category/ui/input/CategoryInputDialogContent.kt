package com.mysty.chefbook.features.category.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.core.android.utils.EmojiUtils
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.textfields.FilledTextField
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.shapes.DialogShape
import com.mysty.chefbook.features.category.R
import com.mysty.chefbook.features.category.ui.input.mvi.CategoryInputDialogIntent
import com.mysty.chefbook.features.category.ui.input.mvi.CategoryInputDialogState

@Composable
internal fun CategoryInputDialogContent(
  state: CategoryInputDialogState,
  onIntent: (CategoryInputDialogIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val isEditing = state.isEditing
  val name = state.input.name
  val cover = state.input.cover.orEmpty()

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val coverPlaceholder = remember { EmojiUtils.randomFoodEmoji() }

  Column(
    modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(12.dp)
        .background(
            color = colors.backgroundPrimary,
            shape = DialogShape,
        )
        .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = stringResource(
        if (isEditing) {
          R.string.common_category_input_dialog_edit_category
        } else {
          R.string.common_category_input_dialog_new_category
        }
      ),
      style = typography.h3,
      color = colors.foregroundPrimary
    )
    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(top = 18.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      FilledTextField(
        value = cover,
        onValueChange = { text -> onIntent(CategoryInputDialogIntent.SetCover(text)) },
        modifier = modifier.width(56.dp),
        textOpacity = if (cover.isNotEmpty()) 1F else 0.5F,
        hint = coverPlaceholder,
      )
      FilledTextField(
        value = name,
        onValueChange = { text -> onIntent(CategoryInputDialogIntent.SetName(text)) },
        modifier = modifier.fillMaxWidth(),
        hint = stringResource(R.string.common_general_name),
      )
    }
    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(top = 24.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      if (isEditing) {
        DynamicButton(
          leftIcon = ImageVector.vectorResource(R.drawable.ic_trash),
          unselectedForeground = colors.foregroundPrimary,
          modifier = Modifier
              .padding(end = 8.dp)
              .width(56.dp)
              .height(44.dp),
          onClick = { onIntent(CategoryInputDialogIntent.Delete) },
        )
      }
      DynamicButton(
        leftIcon = ImageVector.vectorResource(R.drawable.ic_cross),
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
            .weight(1F)
            .fillMaxWidth()
            .height(44.dp),
        onClick = { onIntent(CategoryInputDialogIntent.Cancel) },
      )
      DynamicButton(
        leftIcon = ImageVector.vectorResource(R.drawable.ic_check),
        isSelected = name.isNotEmpty(),
        modifier = Modifier
            .weight(1F)
            .fillMaxWidth()
            .height(44.dp),
        onClick = { onIntent(CategoryInputDialogIntent.ConfirmInput) },
      )
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
    CategoryInputDialogContent(
      state = CategoryInputDialogState(
        input = CategoryInput(
          name = if (!isEditing) Strings.EMPTY else "Cupcake",
        ),
        isEditing = isEditing,
      ),
      onIntent = {},
    )
  }
}
