package com.mysty.chefbook.core.android.compose.providers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import okhttp3.OkHttpClient

@Composable
fun UiDependenciesProvider(
    imageClient: OkHttpClient,
    content: @Composable () -> Unit,
) =
    CompositionLocalProvider(
        LocalImageHttpClient provides imageClient,
        content = content
    )

object LocalDependencies {
    val imageClient: OkHttpClient
        @Composable
        get() = LocalImageHttpClient.current
}

private val LocalImageHttpClient = staticCompositionLocalOf<OkHttpClient> {
    error("No Image OkHttpClient provided")
}

