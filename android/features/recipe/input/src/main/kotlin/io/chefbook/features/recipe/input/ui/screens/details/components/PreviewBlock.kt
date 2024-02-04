package io.chefbook.features.recipe.input.ui.screens.details.components

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.CircleIconButton
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.components.images.EncryptedImage
import io.chefbook.features.recipe.input.R
import io.chefbook.features.recipe.input.ui.images.cropImageOptions
import io.chefbook.features.recipe.input.ui.viewmodel.generatePicturePath
import io.chefbook.design.R as designR

@Composable
internal fun PreviewBlock(
  recipeId: String,
  preview: String?,
  onPreviewSet: (String) -> Unit,
  onPreviewDeleted: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current

  val colors = LocalTheme.colors

  val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
    val uri = result.uriContent?.path
    if (result.isSuccessful && uri != null) onPreviewSet(uri)
  }

  val imagePickerLauncher =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
      if (uri == null) return@rememberLauncherForActivityResult
      val cropOptions = CropImageContractOptions(
        uri = uri,
        cropImageOptions = cropImageOptions(
          outputUri = generatePicturePath(context, recipeId),
          backgroundColor = colors.backgroundPrimary.toArgb(),
          foregroundColor = colors.foregroundPrimary.toArgb(),
          tintColor = colors.tintPrimary.toArgb()
        )
      )
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
        iconId = designR.drawable.ic_cross,
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
