package com.cactusknights.chefbook.ui.screens.recipeinput.dialogs

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
import androidx.compose.runtime.collectAsState
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
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.CircleIconButton
import com.mysty.chefbook.design.components.textfields.ThemedIndicatorTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CaloriesDialog(
    viewModel: RecipeInputScreenViewModel,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val viewModelState = viewModel.state.collectAsState()
    val state = viewModelState.value.input

    Column(
        modifier = Modifier
            .imePadding()
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = stringResource(R.string.common_general_in_100_g),
                maxLines = 1,
                style = typography.h4,
                color = colors.foregroundPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp)
            )
            CircleIconButton(
                iconId = R.drawable.ic_cross,
                onClick = {
                    keyboardController?.hide()
                    viewModel.obtainEvent(RecipeInputScreenEvent.CloseBottomSheet)
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
            onValueChange = { calories ->
                viewModel.obtainEvent(RecipeInputScreenEvent.SetCalories(calories.toIntOrNull()))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            label = {
                Text(
                    stringResource(R.string.common_general_kcal),
                    color = colors.foregroundPrimary
                )
            },
        )
        ThemedIndicatorTextField(
            value = if (state.macronutrients?.protein != null) state.macronutrients.protein.toString() else "",
            modifier = Modifier
                .fillMaxWidth(),
            onValueChange = { protein ->
                viewModel.obtainEvent(RecipeInputScreenEvent.SetProtein(protein.toIntOrNull()))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            label = {
                Text(
                    stringResource(R.string.common_general_protein),
                    color = colors.foregroundPrimary
                )
            },
        )
        ThemedIndicatorTextField(
            value = if (state.macronutrients?.fats != null) state.macronutrients.fats.toString() else "",
            modifier = Modifier
                .fillMaxWidth(),
            onValueChange = { fats ->
                viewModel.obtainEvent(RecipeInputScreenEvent.SetFats(fats.toIntOrNull()))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            label = {
                Text(
                    stringResource(R.string.common_general_fats),
                    color = colors.foregroundPrimary
                )
            },
        )
        ThemedIndicatorTextField(
            value = if (state.macronutrients?.carbohydrates != null) state.macronutrients.carbohydrates.toString() else "",
            modifier = Modifier
                .fillMaxWidth(),
            onValueChange = { carbs ->
                viewModel.obtainEvent(RecipeInputScreenEvent.SetCarbohydrates(carbs.toIntOrNull()))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
                viewModel.obtainEvent(RecipeInputScreenEvent.CloseBottomSheet)
            },
            label = {
                Text(
                    stringResource(R.string.common_general_carbs),
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