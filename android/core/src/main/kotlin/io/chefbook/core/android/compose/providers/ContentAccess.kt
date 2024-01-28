package io.chefbook.core.android.compose.providers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun ContentAccessProvider(
    isEncrypted: Boolean,
    content: @Composable () -> Unit,
) =
    ContentAccessProvider(
        contentType = if (isEncrypted) ContentType.ENCRYPTED else ContentType.DECRYPTED,
        content = content
    )

@Composable
fun ContentAccessProvider(
    contentType: ContentType,
    content: @Composable () -> Unit,
) =
    CompositionLocalProvider(
        LocalContentType provides contentType,
        content = content
    )

object LocalDataAccess {
    val type: ContentType
        @Composable
        get() = LocalContentType.current
}

private val LocalContentType = staticCompositionLocalOf {
    ContentType.DECRYPTED
}

enum class ContentType {
    DECRYPTED, DECRYPTABLE, ENCRYPTED
}
