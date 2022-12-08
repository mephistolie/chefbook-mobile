package com.cactusknights.chefbook.ui.views.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.cactusknights.chefbook.core.ui.DataAccess
import com.cactusknights.chefbook.core.ui.DataType
import com.cactusknights.chefbook.core.ui.Dependencies

private const val TYPE = "TYPE"

@Composable
fun EncryptedImage(
    data: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
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
    val dataType = DataAccess.type
    val imageLoader = if (dataType == DataType.DECRYPTABLE) {
        ImageLoader.Builder(context)
            .okHttpClient(Dependencies.decryptImageClient)
            .build()
    } else {
        context.imageLoader
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
            .data(data)
            .crossfade(true)
            .build(),
        imageLoader = imageLoader,
        placeholder = placeholder,
        contentDescription = contentDescription,
        modifier = modifier,
        error = error,
        fallback = fallback,
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
