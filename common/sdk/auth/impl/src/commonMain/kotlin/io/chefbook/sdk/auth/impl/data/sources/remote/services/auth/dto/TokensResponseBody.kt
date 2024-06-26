package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TokensResponse(
  @SerialName("accessToken")
  val accessToken: String,
  @SerialName("refreshToken")
  val refreshToken: String,
  @SerialName("expiresAt")
  val expiresAt: String,
  @SerialName("profileDeletingAt")
  val profileDeletingAt: String? = null,
)

internal fun TokensResponse.toSessionInfo() =
  Session(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expirationTimestamp = expiresAt,
    profileDeletionTimestamp = profileDeletingAt,
  )
