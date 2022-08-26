package com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.CircleImageButton
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

@Composable
fun ColumnScope.PreviewBlock(
    preview: String?,
    onPreviewSet: (String) -> Unit,
    onPreviewDeleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val colors = ChefBookTheme.colors

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        val uri = result.getUriFilePath(context)
        if (result.isSuccessful && uri != null) {
            onPreviewSet(uri)
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

    AnimatedVisibility(preview != null) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.TopEnd,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(preview)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
            CircleImageButton(
                image = ImageVector.vectorResource(R.drawable.ic_cross),
                onClick = onPreviewDeleted,
                modifier = Modifier
                    .padding(12.dp)
                    .size(32.dp),
                background = colors.foregroundPrimary.copy(alpha = 0.25F),
                tint = colors.backgroundPrimary
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
