package com.mysty.chefbook.core.ui.compose.providers

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
        LocalDataAccess provides contentType,
        content = content
    )

object DataAccess {
    val type: ContentType
        @Composable
        get() = LocalDataAccess.current
}

private val LocalDataAccess = staticCompositionLocalOf {
    ContentType.DECRYPTED
}

enum class ContentType {
    DECRYPTED, DECRYPTABLE, ENCRYPTED
}
