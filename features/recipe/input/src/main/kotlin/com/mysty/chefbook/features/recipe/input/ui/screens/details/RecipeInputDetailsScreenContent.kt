package com.mysty.chefbook.features.recipe.input.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.toolbar.Toolbar
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape24
import com.mysty.chefbook.features.recipe.input.R
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputDetailsScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import com.mysty.chefbook.features.recipe.input.ui.screens.details.components.CaloriesBlock
import com.mysty.chefbook.features.recipe.input.ui.screens.details.components.DescriptionBlock
import com.mysty.chefbook.features.recipe.input.ui.screens.details.components.NameBlock
import com.mysty.chefbook.features.recipe.input.ui.screens.details.components.ParametersBlock
import com.mysty.chefbook.features.recipe.input.ui.screens.details.components.PreviewBlock
import com.mysty.chefbook.features.recipe.input.ui.screens.details.components.ServingsBlock
import com.mysty.chefbook.features.recipe.input.ui.screens.details.components.TimeBlock

@Composable
internal fun RecipeInputDetailsScreenContent(
  state: RecipeInput,
  onIntent: (RecipeInputScreenIntent) -> Unit,
  onDetailsIntent: (RecipeInputDetailsScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .statusBarsPadding()
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Toolbar(
      leftButtonIconId = R.drawable.ic_cross,
      onLeftButtonClick = { onIntent(RecipeInputScreenIntent.Back) },
      modifier = Modifier.padding(horizontal = 12.dp)
    ) {
      Text(
        text = stringResource(R.string.common_general_details),
        maxLines = 1,
        style = typography.h4,
        color = colors.foregroundPrimary,
      )
    }
    LazyColumn(
      modifier = Modifier
        .weight(1F)
        .fillMaxHeight(),
      horizontalAlignment = Alignment.Start,
    ) {
      item {
        PreviewBlock(
          preview = state.preview,
          onPreviewSet = { uri -> onDetailsIntent(RecipeInputDetailsScreenIntent.SetPreview(uri)) },
          onPreviewDeleted = { onDetailsIntent(RecipeInputDetailsScreenIntent.RemovePreview) },
          modifier = Modifier
            .padding(
              start = 12.dp,
              top = 12.dp,
              end = 12.dp,
            )
            .fillMaxWidth()
            .aspectRatio(2F)
            .clippedBackground(colors.backgroundSecondary, RoundedCornerShape24),
        )
      }
      item {
        NameBlock(
          name = state.name,
          onValueChange = { name ->
            onDetailsIntent(RecipeInputDetailsScreenIntent.SetName(name))
          },
          modifier = Modifier
            .padding(
              start = 12.dp,
              top = 12.dp,
              end = 12.dp,
            )
            .fillMaxWidth(),
        )
      }
      item {
        ParametersBlock(
          state = state,
          onVisibilityClick = { onDetailsIntent(RecipeInputDetailsScreenIntent.OpenVisibilityPicker) },
          onLanguageClick = { onDetailsIntent(RecipeInputDetailsScreenIntent.OpenLanguagePicker) },
          onEncryptionClick = { onDetailsIntent(RecipeInputDetailsScreenIntent.OpenEncryptedStatePicker) },
        )
      }
      item {
        ServingsBlock(
          state = state,
          onSetServings = { servings ->
            onDetailsIntent(RecipeInputDetailsScreenIntent.SetServings(servings))
          },
          modifier = Modifier
            .padding(
              start = 12.dp,
              top = 16.dp,
              end = 12.dp,
            )
            .fillMaxWidth()
            .height(56.dp),
        )
      }
      item {
        TimeBlock(
          state = state,
          onTimeInput = { hours, minutes ->
            onDetailsIntent(RecipeInputDetailsScreenIntent.SetTime(h = hours, min = minutes))
          },
          modifier = Modifier
            .padding(
              start = 12.dp,
              top = 4.dp,
              end = 12.dp,
            )
            .fillMaxWidth()
            .height(56.dp),
        )
      }
      item {
        CaloriesBlock(
          state = state,
          onCaloriesClick = { onDetailsIntent(RecipeInputDetailsScreenIntent.OpenCaloriesDialog) },
          modifier = Modifier
            .padding(
              start = 12.dp,
              top = 4.dp,
              end = 12.dp,
            )
            .fillMaxWidth()
            .height(56.dp),
        )
      }
      item {
        DescriptionBlock(
          description = state.description.orEmpty(),
          modifier = Modifier
            .padding(
              start = 12.dp,
              top = 4.dp,
              end = 12.dp,
              bottom = 60.dp,
            )
            .fillMaxWidth(),
          onValueChange = { description ->
            onDetailsIntent(RecipeInputDetailsScreenIntent.SetDescription(description))
          }
        )
      }
    }
    DynamicButton(
      text = stringResource(R.string.common_general_continue),
      isSelected = isContinueAvailable(state),
      isEnabled = isContinueAvailable(state),
      cornerRadius = 16.dp,
      modifier = Modifier
        .navigationBarsPadding()
        .padding(
          start = 12.dp,
          top = 8.dp,
          end = 12.dp,
          bottom = 4.dp,
        )
        .fillMaxWidth()
        .height(56.dp),
      onClick = { onIntent(RecipeInputScreenIntent.Continue) },
    )
  }
}

private fun isContinueAvailable(input: RecipeInput) =
  input.name.isNotBlank()
