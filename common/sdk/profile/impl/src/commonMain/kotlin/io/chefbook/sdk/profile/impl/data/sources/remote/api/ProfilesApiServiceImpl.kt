package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.clients.ProfileHttpClientFactory
import io.chefbook.sdk.network.api.internal.service.RequestHandler
import io.chefbook.sdk.profile.impl.data.sources.common.dto.ProfileSerializable
import io.ktor.client.request.get

internal open class ProfilesApiServiceImpl(
  private val clientFactory: ProfileHttpClientFactory,
) : RequestHandler(), ProfilesApiService {

  override suspend fun getProfile(
    profileId: String,
  ): Result<ProfileSerializable> = safeRequest {
    val client = clientFactory.getOrCreate(profileId)
    client.get(PROFILE_ROUTE)
  }

  companion object {
    const val PROFILE_ROUTE = "/v1/profile"
  }
}
