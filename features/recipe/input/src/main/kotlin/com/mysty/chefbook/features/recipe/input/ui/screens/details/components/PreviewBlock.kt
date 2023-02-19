package com.mysty.chefbook.features.recipe.input.ui.screens.details.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.CircleIconButton
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.images.EncryptedImage
import com.mysty.chefbook.features.recipe.input.R

@Composable
internal fun PreviewBlock(
  preview: String?,
  onPreviewSet: (String) -> Unit,
  onPreviewDeleted: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  val colors = LocalTheme.colors

  val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
    val uri = result.getUriFilePath(context)
    if (result.isSuccessful && uri != null) {
      onPreviewSet(uri)
    }
  }

  val imagePickerLauncher =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
      val cropOptions = CropImageContractOptions(uri, CropImageOptions())
        .setInitialCropWindowPaddingRatio(0F)
        .setOutputCompressQuality(100)
        .setGuidelines(CropImageView.Guidelines.ON)
        .setAspectRatio(1, 1)
        .setAllowRotation(false)
        .setAllowFlipping(false)
        .setImageSource(includeGallery = true, includeCamera = false)
        .setCropMenuCropButtonIcon(R.drawable.ic_check)
      imageCropLauncher.launch(cropOptions)
    }

  AnimatedVisibility(preview != null) {
    Box(
      modifier = modifier,
      contentAlignment = Alignment.TopEnd,
    ) {
      EncryptedImage(
        data = preview,
        modifier = Modifier.matchParentSize(),
      )
      CircleIconButton(
        iconId = R.drawable.ic_cross,
        onClick = onPreviewDeleted,
        modifier = Modifier
            .padding(12.dp)
            .size(32.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
        tint = Color.White
      )
    }
  }
  AnimatedVisibility(preview == null) {
    DynamicButton(
      text = stringResource(R.string.common_recipe_input_screen_add_cover),
      unselectedForeground = colors.foregroundPrimary,
      cornerRadius = 16.dp,
      modifier = Modifier
          .padding(
              start = 12.dp,
              top = 12.dp,
              end = 12.dp,
          )
          .fillMaxWidth()
          .height(56.dp),
      onClick = { imagePickerLauncher.launch("image/*") },
    )
  }
}