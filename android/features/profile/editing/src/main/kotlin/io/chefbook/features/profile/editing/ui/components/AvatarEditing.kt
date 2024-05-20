package io.chefbook.features.profile.editing.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.CircleIconButton
import io.chefbook.features.profile.editing.ui.getProfileAvatarPath
import io.chefbook.design.R as designR

private fun cropImageOptions(
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
  cropMenuCropButtonIcon = designR.drawable.ic_check,
  activityMenuIconColor = tintColor,
  activityBackgroundColor = backgroundColor,
  customOutputUri = outputUri,
  toolbarColor = backgroundColor,
  toolbarBackButtonColor = foregroundColor,
  toolbarTintColor = tintColor,
)

@Composable
internal fun AvatarEditing(
  avatar: String?,
  onAvatarSet: (String) -> Unit,
  onDeleteClick: () -> Unit,
) {
  val colors = LocalTheme.colors

  val context = LocalContext.current

  val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
    val uri = result.uriContent?.path
    if (result.isSuccessful && uri != null) onAvatarSet(uri)
  }

  val imagePickerLauncher =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
      if (uri == null) return@rememberLauncherForActivityResult
      val cropOptions = CropImageContractOptions(
        uri = uri,
        cropImageOptions = cropImageOptions(
          outputUri = getProfileAvatarPath(context),
          backgroundColor = colors.backgroundPrimary.toArgb(),
          foregroundColor = colors.foregroundPrimary.toArgb(),
          tintColor = colors.tintPrimary.toArgb()
        )
      )
      imageCropLauncher.launch(cropOptions)
    }

  val offset = animateDpAsState(
    label = "avatar_buttons_offset",
    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
    targetValue = if (avatar != null) 104.dp else 0.dp,
  )

  Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center,
  ) {
    CircleIconButton(
      iconId = designR.drawable.ic_camera,
      onClick = { imagePickerLauncher.launch("image/*") },
      modifier = Modifier
        .size(56.dp)
        .offset(x = -offset.value),
    )
    CircleIconButton(
      iconId = designR.drawable.ic_trash,
      onClick = onDeleteClick,
      modifier = Modifier
        .size(56.dp)
        .offset(x = offset.value),
    )
    CircleIconButton(
      iconId = designR.drawable.ic_camera,
      onClick = { imagePickerLauncher.launch("image/*") },
      modifier = Modifier.size(96.dp),
      iconModifier = Modifier.size(48.dp),
    )
    AnimatedVisibility(
      visible = avatar != null,
      enter = fadeIn(),
      exit = fadeOut(),
    ) {
      AsyncImage(
        model = ImageRequest.Builder(context)
          .data(avatar)
          .crossfade(true)
          .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(96.dp)
          .clip(CircleShape)
      )
    }
  }
}
