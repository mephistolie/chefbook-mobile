package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class TokensResponse(
  @SerialName("profileId")
  val profileId: String,
  @SerialName("accessToken")
  val accessToken: String,
  @SerialName("refreshToken")
  val refreshToken: String,
  @SerialName("expirationTimestamp")
  val expirationTimestamp: String,
  @SerialName("profileDeletionTimestamp")
  val profileDeletionTimestamp: String? = null,
)

internal fun TokensResponse.toSessionInfo() =
  Session(
    profileId = profileId,
    accessToken = accessToken,
    refreshToken = refreshToken,
    expirationTimestamp = expirationTimestamp,
    profileDeletionTimestamp = profileDeletionTimestamp,
  )
