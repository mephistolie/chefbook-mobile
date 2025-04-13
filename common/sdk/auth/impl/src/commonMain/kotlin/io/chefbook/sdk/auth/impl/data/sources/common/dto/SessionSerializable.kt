package io.chefbook.sdk.auth.impl.data.sources.common.dto

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class SessionSerializable(
  @SerialName("profileId")
  val profileId: String,
  @SerialName("accessToken")
  val accessToken: String? = null,
  @SerialName("refreshToken")
  val refreshToken: String? = null,
  @SerialName("expirationTimestamp")
  val expirationTimestamp: String? = null,
  @SerialName("profileDeletionTimestamp")
  val profileDeletionTimestamp: String? = null,
)

internal fun SessionSerializable.deserialize(): Session =
  Session(
    profileId = profileId,
    accessToken = accessToken,
    refreshToken = refreshToken,
    expirationTimestamp = expirationTimestamp,
    profileDeletionTimestamp = profileDeletionTimestamp,
  )

internal fun Session.serialize() =
  SessionSerializable(
    profileId = profileId,
    accessToken = accessToken,
    refreshToken = refreshToken,
    expirationTimestamp = expirationTimestamp,
    profileDeletionTimestamp = profileDeletionTimestamp,
  )
