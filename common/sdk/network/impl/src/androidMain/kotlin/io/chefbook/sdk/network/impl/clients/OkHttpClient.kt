package io.chefbook.sdk.network.impl.clients

import okhttp3.Interceptor
import okhttp3.OkHttpClient

fun okHttpClient(
  interceptors: List<Interceptor> = emptyList(),
) = OkHttpClient.Builder()
  .apply {
    interceptors.forEach(::addInterceptor)
  }
  .build()
