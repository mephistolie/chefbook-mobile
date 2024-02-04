package io.chefbook.features.recipe.input.ui.screens.cooking

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.components.toolbar.Toolbar
import io.chefbook.features.recipe.input.ui.images.cropImageOptions
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputCookingScreenIntent
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenIntent
import io.chefbook.features.recipe.input.ui.screens.cooking.components.AddCookingItemBlock
import io.chefbook.features.recipe.input.ui.screens.cooking.components.cookingItemList
import io.chefbook.features.recipe.input.ui.viewmodel.generatePicturePath
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput.CookingItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import io.chefbook.core.android.R as coreR

@Composable
internal fun RecipeInputCookingScreenDisplay(
  state: RecipeInput,
  onIntent: (RecipeInputScreenIntent) -> Unit,
  onCookingIntent: (RecipeInputCookingScreenIntent) -> Unit,
) {
  val context = LocalContext.current

  val haptic = LocalHapticFeedback.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  var focusStepIndex: Int? = null

  val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
    focusStepIndex?.let { index ->
      val uri = result.uriContent?.path
      if (result.isSuccessful && uri != null) {
        onCookingIntent(RecipeInputCookingScreenIntent.AddStepPicture(index, uri))
      }
    }
  }

  val imagePickerLauncher =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
      val cropOptions = CropImageContractOptions(uri, cropImageOptions(
      outputUri = generatePicturePath(context, state.id),
      backgroundColor = colors.backgroundPrimary.toArgb(),
      foregroundColor = colors.foregroundPrimary.toArgb(),
      tintColor = colors.tintPrimary.toArgb()
    ))
      if (uri != null) imageCropLauncher.launch(cropOptions)
    }

  val reorderableState = rememberReorderableLazyListState({ from, to ->
    onCookingIntent(RecipeInputCookingScreenIntent.MoveStepItem(from.index, to.index))
  })

  Column(
    modifier = Modifier
      .statusBarsPadding()
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Toolbar(
      onLeftButtonClick = { onIntent(RecipeInputScreenIntent.Back) },
      modifier = Modifier.padding(horizontal = 12.dp)
    ) {
      Text(
        text = stringResource(coreR.string.common_general_cooking),
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
      cookingItemList(
        cooking = state.cooking,
        onIntent = onCookingIntent,
        reorderableState = reorderableState,
        haptic = haptic,
        onFocusStep = { index -> focusStepIndex = index },
        imagePickerLauncher = imagePickerLauncher,
      )
      item {
        AddCookingItemBlock(
          onIntent = onCookingIntent,
          modifier = Modifier
            .fillMaxWidth()
            .padding(
              start = 12.dp,
              top = 16.dp,
              end = 12.dp,
              bottom = 32.dp
            )
        )
      }
    }
    val isContinueAvailable = isContinueAvailable(state.cooking)
    DynamicButton(
      text = stringResource(coreR.string.common_general_save),
      isSelected = isContinueAvailable,
      isEnabled = isContinueAvailable,
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
      onClick = { onIntent(RecipeInputScreenIntent.Save) },
    )
  }
}

private fun isContinueAvailable(cooking: List<CookingItem>) =
  cooking.any { it is CookingItem.Step && it.description.isNotBlank() }
