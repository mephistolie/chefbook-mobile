package io.chefbook.features.recipe.input.ui.dialogs.calories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.CircleIconButton
import io.chefbook.design.components.textfields.ThemedIndicatorTextField
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputDetailsScreenIntent
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CaloriesDialogContent(
  state: RecipeInput,
  onIntent: (RecipeInputScreenIntent) -> Unit,
  onDetailsIntent: (RecipeInputDetailsScreenIntent) -> Unit,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusRequester = remember { FocusRequester() }

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography


  Column(
    modifier = Modifier
      .imePadding()
      .clippedBackground(colors.backgroundPrimary, shape = RoundedCornerShape28Top)
      .padding(horizontal = 18.dp)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Box(
      contentAlignment = Alignment.TopEnd
    ) {
      Text(
        text = stringResource(coreR.string.common_general_in_100_g),
        maxLines = 1,
        style = typography.h4,
        color = colors.foregroundPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 18.dp)
      )
      CircleIconButton(
        iconId = designR.drawable.ic_cross,
        onClick = {
          keyboardController?.hide()
          onIntent(RecipeInputScreenIntent.CloseBottomSheet)
        },
        modifier = Modifier
          .padding(top = 18.dp)
          .size(28.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
        tint = Color.White
      )
    }
    Divider(
      color = colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    ThemedIndicatorTextField(
      value = if (state.calories != null) state.calories.toString() else "",
      modifier = Modifier
        .focusRequester(focusRequester)
        .fillMaxWidth(),
      onValueChange = { text ->
        if (text.isDigitsOnly()) {
          onDetailsIntent(RecipeInputDetailsScreenIntent.SetCalories(text.toIntOrNull()))
        }
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
      ),
      label = {
        Text(
          stringResource(coreR.string.common_general_kcal),
          color = colors.foregroundPrimary
        )
      },
    )
    ThemedIndicatorTextField(
      value = state.macronutrients?.protein?.toString() ?: "",
      modifier = Modifier.fillMaxWidth(),
      onValueChange = { text ->
        if (text.isDigitsOnly()) {
          onDetailsIntent(RecipeInputDetailsScreenIntent.SetProtein(text.toIntOrNull()))
        }
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
      ),
      label = {
        Text(
          stringResource(coreR.string.common_general_protein),
          color = colors.foregroundPrimary
        )
      },
    )
    ThemedIndicatorTextField(
      value = state.macronutrients?.fats?.toString() ?: "",
      modifier = Modifier
        .fillMaxWidth(),
      onValueChange = { text ->
        if (text.isDigitsOnly()) {
          onDetailsIntent(RecipeInputDetailsScreenIntent.SetFats(text.toIntOrNull()))
        }
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
      ),
      label = {
        Text(
          stringResource(coreR.string.common_general_fats),
          color = colors.foregroundPrimary
        )
      },
    )
    ThemedIndicatorTextField(
      value = state.macronutrients?.carbohydrates?.toString() ?: "",
      modifier = Modifier.fillMaxWidth(),
      onValueChange = { text ->
        if (text.isDigitsOnly()) {
         onDetailsIntent(RecipeInputDetailsScreenIntent.SetCarbohydrates(text.toIntOrNull()))
        }
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions {
        keyboardController?.hide()
        onIntent(RecipeInputScreenIntent.CloseBottomSheet)
      },
      label = {
        Text(
          stringResource(coreR.string.common_general_carbs),
          color = colors.foregroundPrimary
        )
      },
    )
    Spacer(modifier = Modifier.height(12.dp))

    LaunchedEffect(Unit) {
      focusRequester.requestFocus()
    }
  }
}