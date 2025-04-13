package io.chefbook.sdk.network.api.internal.clients

import io.ktor.client.HttpClient

interface ProfileHttpClientFactory {

  val baseClient: HttpClient

  fun getOrCreate(profileId: String): HttpClient

  fun get(profileId: String): HttpClient?
}
