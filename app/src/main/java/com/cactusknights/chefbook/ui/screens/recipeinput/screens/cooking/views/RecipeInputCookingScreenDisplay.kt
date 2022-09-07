package com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.cooking.views.elements.StepField
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.views.elements.SectionField
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import com.cactusknights.chefbook.ui.views.common.Toolbar
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun RecipeInputCookingScreenDisplay(
    state: RecipeInput,
    onEvent: (RecipeInputScreenEvent) -> Unit,
) {
    val context = LocalContext.current

    val haptic = LocalHapticFeedback.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    var focusStepIndex: Int? = null

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        focusStepIndex?.let { index ->
            val uri = result.getUriFilePath(context)
            if (result.isSuccessful && uri != null) {
                onEvent(RecipeInputScreenEvent.AddStepPicture(index, uri))
            }
        }
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            val cropOptions = CropImageContractOptions(uri, CropImageOptions())
                .setScaleType(CropImageView.ScaleType.CENTER_CROP)
                .setInitialCropWindowPaddingRatio(0F)
                .setOutputCompressQuality(100)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(5, 4)
            imageCropLauncher.launch(cropOptions)
        }

    val reorderableState = rememberReorderableLazyListState({ from, to ->
        onEvent(RecipeInputScreenEvent.MoveStepItem(from.index, to.index))
    })

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Toolbar(
            onLeftButtonClick = { onEvent(RecipeInputScreenEvent.Back) },
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.common_general_cooking),
                maxLines = 1,
                style = typography.h4,
                color = colors.foregroundPrimary,
            )
        }
        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .reorderable(reorderableState)
                .detectReorderAfterLongPress(reorderableState),
            horizontalAlignment = Alignment.Start,
        ) {
            itemsIndexed(state.cooking, key = { _, item -> item.id}) { index, item ->
                ReorderableItem(
                    reorderableState = reorderableState,
                    key = item.id,
                ) { isDragging ->
                    if (isDragging) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    when (item) {
                        is CookingItem.Section -> {
                            SectionField(
                                name = item.name,
                                onNameChange = { name -> onEvent(RecipeInputScreenEvent.SetCookingItemValue(index, name)) },
                                onDeleteClick = { onEvent(RecipeInputScreenEvent.DeleteStepItem(index)) },
                            )
                        }
                        is CookingItem.Step -> {
                            StepField(
                                step = item,
                                number = state.cooking.subList(0, index).filterIsInstance<CookingItem.Step>().size + 1,
                                onDescriptionChange = { name -> onEvent(RecipeInputScreenEvent.SetCookingItemValue(index, name)) },
                                onAddPictureClick = {
                                    focusStepIndex = index
                                    imagePickerLauncher.launch("image/*")
                                },
                                onDeletePictureClick = { pictureIndex ->
                                    onEvent(RecipeInputScreenEvent.DeleteStepPicture(
                                        stepIndex = index,
                                        pictureIndex = pictureIndex
                                    ))
                                },
                                onDeleteClick = { onEvent(RecipeInputScreenEvent.DeleteStepItem(index)) }
                            )
                        }
                        else -> Unit
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 16.dp, 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DynamicButton(
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
                        text = stringResource(R.string.common_general_section),
                        unselectedForeground = colors.foregroundPrimary,
                        modifier = Modifier
                            .height(36.dp),
                        onClick = { onEvent(RecipeInputScreenEvent.AddCookingSection) }
                    )
                    DynamicButton(
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
                        text = stringResource(R.string.common_general_step),
                        unselectedForeground = colors.foregroundPrimary,
                        modifier = Modifier
                            .height(36.dp),
                        onClick = { onEvent(RecipeInputScreenEvent.AddStep) }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        AnimatedVisibility(
            state.cooking.any { it is CookingItem.Step && it.description.isNotBlank() }
        ) {
            DynamicButton(
                text = stringResource(R.string.common_general_save),
                isSelected = true,
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
                onClick = { onEvent(RecipeInputScreenEvent.Save) },
            )
        }
    }
}

