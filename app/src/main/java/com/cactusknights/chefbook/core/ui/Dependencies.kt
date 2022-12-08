package com.cactusknights.chefbook.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import okhttp3.OkHttpClient

@Composable
fun DependenciesProvider(
    imageClient: OkHttpClient,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalImageHttpClient provides imageClient,
        content = content
    )
}

object Dependencies {
    val decryptImageClient: OkHttpClient
        @Composable
        get() = LocalImageHttpClient.current
}

private val LocalImageHttpClient = staticCompositionLocalOf<OkHttpClient> {
    error("No ChefBook Image OkHttpClient provided")
}