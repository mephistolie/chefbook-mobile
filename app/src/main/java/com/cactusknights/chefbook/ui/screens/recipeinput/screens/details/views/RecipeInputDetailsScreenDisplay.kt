package com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views

import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks.CaloriesBlock
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks.ParametersBlock
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks.PreviewBlock
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks.ServingsBlock
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import com.cactusknights.chefbook.ui.views.common.Toolbar
import com.cactusknights.chefbook.ui.views.textfields.IndicatorTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeInputDetailsScreenDisplay(
    state: RecipeInput,
    onEvent: (RecipeInputScreenEvent) -> Unit,
) {
    val context = LocalContext.current
    val resources = context.resources

    val keyboardController = LocalSoftwareKeyboardController.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val timePicker = TimePickerDialog(
        context,
        { _, hourOfDay, minute -> onEvent(RecipeInputScreenEvent.SetTime(hourOfDay, minute)) },
        if (state.time != null && state.time > 0) state.time / 60 else 0,
        if (state.time != null && state.time > 0) state.time % 60 else 15,
        true,
    )

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Toolbar(
            leftButtonIcon = ImageVector.vectorResource(R.drawable.ic_cross),
            onLeftButtonClick = {
                if (state.name.isNotEmpty()) {
                    onEvent(RecipeInputScreenEvent.ChangeCancelDialogState(true))
                } else {
                    onEvent(RecipeInputScreenEvent.Back)
                }
            },
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
                    onPreviewSet = { uri ->
                        onEvent(
                            RecipeInputScreenEvent.SetPreview(
                                uri,
                                context
                            )
                        )
                    },
                    onPreviewDeleted = { onEvent(RecipeInputScreenEvent.RemovePreview) },
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            top = 12.dp,
                            end = 12.dp,
                        )
                        .fillMaxWidth()
                        .aspectRatio(2F)
                        .clip(RoundedCornerShape(24.dp))
                        .background(colors.backgroundSecondary),
                )
            }
            item {
                IndicatorTextField(
                    value = state.name,
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            top = 12.dp,
                            end = 12.dp,
                        )
                        .fillMaxWidth(),
                    onValueChange = { name -> onEvent(RecipeInputScreenEvent.SetName(name)) },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    label = {
                        Text(
                            stringResource(R.string.common_general_name),
                            color = colors.foregroundPrimary
                        )
                    },
                    maxLines = 1,
                )
            }
            item {
                ParametersBlock(
                    state = state,
                    onVisibilityClick = { onEvent(RecipeInputScreenEvent.OpenVisibilityPicker) },
                    onLanguageClick = { onEvent(RecipeInputScreenEvent.OpenLanguagePicker) },
                    onEncryptionClick = { onEvent(RecipeInputScreenEvent.OpenEncryptedStatePicker) },
                )
            }
            item {
                ServingsBlock(
                    state = state,
                    onSetServings = { servings -> onEvent(RecipeInputScreenEvent.SetServings(servings)) },
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
                Row(
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            top = 4.dp,
                            end = 12.dp,
                        )
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.common_general_time),
                        style = typography.headline1,
                        color = colors.foregroundPrimary,
                    )
                    DynamicButton(
                        text = if (state.time != null && state.time > 0)
                            Utils.minutesToTimeString(state.time, resources)
                        else
                            stringResource(R.string.common_general_specify),
                        unselectedForeground = colors.foregroundPrimary,
                        onClick = { timePicker.show() },
                        modifier = Modifier
                            .requiredWidth(128.dp)
                            .height(38.dp),
                    )
                }
            }
            item {
                CaloriesBlock(
                    state = state,
                    onCaloriesClick = { onEvent(RecipeInputScreenEvent.OpenCaloriesDialog) },
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
                IndicatorTextField(
                    value = state.description.orEmpty(),
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            top = 4.dp,
                            end = 12.dp,
                            bottom = 28.dp,
                        )
                        .fillMaxWidth(),
                    onValueChange = { description ->
                        onEvent(
                            RecipeInputScreenEvent.SetDescription(
                                description
                            )
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.common_general_description),
                            color = colors.foregroundPrimary
                        )
                    },
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        AnimatedVisibility(isContinueAvailable(state)) {
            DynamicButton(
                text = stringResource(R.string.common_general_continue),
                isSelected = isContinueAvailable(state),
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
                onClick = { onEvent(RecipeInputScreenEvent.Continue) },
            )
        }
    }
}

private fun isContinueAvailable(input: RecipeInput) =
    input.name.isNotBlank()