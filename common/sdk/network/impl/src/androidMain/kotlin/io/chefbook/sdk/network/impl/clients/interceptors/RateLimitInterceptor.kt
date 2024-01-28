package io.chefbook.sdk.network.impl.clients.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.Semaphore

private const val RATE_LIMIT = 8

object RateLimitInterceptor : Interceptor {

  private val rateLimiter = Semaphore(RATE_LIMIT)

  override fun intercept(chain: Interceptor.Chain): Response {
    rateLimiter.acquire()
    val response = chain.proceed(chain.request())
    rateLimiter.release()

    return response
  }
}
