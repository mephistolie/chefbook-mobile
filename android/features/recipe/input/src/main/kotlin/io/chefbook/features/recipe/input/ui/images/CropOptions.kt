package io.chefbook.features.recipe.input.ui.images

import android.net.Uri
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import io.chefbook.design.R

fun cropImageOptions(
  outputUri: Uri,
  backgroundColor: Int,
  foregroundColor: Int,
  tintColor: Int,
) = CropImageOptions(
  initialCropWindowPaddingRatio = 0F,
  fixAspectRatio = true,
  aspectRatioX = 1,
  aspectRatioY = 1,
  outputCompressQuality = 100,
  guidelines = CropImageView.Guidelines.ON,
  allowRotation = false,
  allowFlipping = false,
  imageSourceIncludeCamera = false,
  cropMenuCropButtonIcon = R.drawable.ic_check,
  activityMenuIconColor = tintColor,
  activityBackgroundColor = backgroundColor,
  customOutputUri = outputUri,
  toolbarColor = backgroundColor,
  toolbarBackButtonColor = foregroundColor,
  toolbarTintColor = tintColor,
)
