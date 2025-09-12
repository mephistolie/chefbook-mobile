package io.chefbook.sdk.network.impl.clients.plugins

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.api.*
import kotlinx.coroutines.sync.Semaphore

private const val PluginName = "RateLimiter"
private const val RateLimit = 8

val RateLimiter: ClientPlugin<Unit>
  get() {
    val semaphore = Semaphore(RateLimit)

    return createClientPlugin(PluginName) {
      onRequest { _, _ ->
        semaphore.acquire()
      }
      onResponse {
        semaphore.release()
      }
    }
  }
