package io.chefbook.design.components.images

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import io.chefbook.core.android.compose.providers.ContentType
import io.chefbook.core.android.compose.providers.LocalDataAccess
import io.chefbook.core.android.compose.providers.ui.LocalDependencies
import io.chefbook.libs.logger.Logger

private const val TYPE = "TYPE"

@Composable
fun EncryptedImage(
  data: Any?,
  modifier: Modifier = Modifier,
  placeholder: Drawable? = null,
  errorDrawable: Drawable? = placeholder,
  contentDescription: String? = null,
  onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
  onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
  onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
  alignment: Alignment = Alignment.Center,
  contentScale: ContentScale = ContentScale.Crop,
  alpha: Float = DefaultAlpha,
  colorFilter: ColorFilter? = null,
  filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
  val context = LocalContext.current
  val dataType = LocalDataAccess.type
  val imageClient = LocalDependencies.imageClient

  val imageLoader = remember(data) {
    if (dataType == ContentType.DECRYPTABLE) {
      ImageLoader.Builder(context)
        .okHttpClient(imageClient)
        .build()
    } else {
      context.imageLoader
    }
  }

  val diskKey = "${data}_${dataType.name}"
  val memoryKey = MemoryCache.Key(
    key = data.toString(),
    extras = mapOf(TYPE to dataType.name)
  )

  AsyncImage(
    model = ImageRequest.Builder(context)
      .diskCacheKey(diskKey)
      .memoryCacheKey(memoryKey)
      .placeholder(placeholder)
      .error(errorDrawable)
      .data(data)
      .crossfade(true)
      .build(),
    imageLoader = imageLoader,
    contentDescription = contentDescription,
    modifier = modifier,
    onLoading = onLoading,
    onSuccess = onSuccess,
    onError = onError,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    filterQuality = filterQuality,
  )
}
