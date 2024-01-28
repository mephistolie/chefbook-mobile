package io.chefbook.sdk.network.impl.clients.configuration

import android.content.Context
import android.os.Build
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.UserAgent

internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.installUserAgent(context: Context) {
  install(UserAgent) {
    val packageManager = context.packageManager.getPackageInfo(context.packageName, 0)
    val configuration = context.resources.configuration

    val version = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      packageManager.longVersionCode
    } else {
      packageManager.versionCode.toLong()
    }

    val model = Build.MODEL
    val androidVersion = Build.VERSION.RELEASE
    val buildNumber = Build.DISPLAY

    val languageCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      configuration.locales[0].toString()
    } else {
      configuration.locale.toString()
    }

    agent = "ChefBook/$version (Linux; U; Android $androidVersion; $languageCode; $model; Build/$buildNumber)"
  }
}