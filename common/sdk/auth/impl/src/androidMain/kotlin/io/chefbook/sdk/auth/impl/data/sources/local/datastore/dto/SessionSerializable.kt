package io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionSerializable(
  @SerialName("accessToken")
  val accessToken: String? = null,
  @SerialName("refreshToken")
  val refreshToken: String? = null,
  @SerialName("expirationTimestamp")
  val expirationTimestamp: String? = null,
  @SerialName("profileDeletionTimestamp")
  val profileDeletionTimestamp: String? = null,
)

fun SessionSerializable.toModel() =
  if (accessToken != null && refreshToken != null) {
    Session(
      accessToken = accessToken,
      refreshToken = refreshToken,
      expirationTimestamp = expirationTimestamp,
      profileDeletionTimestamp = profileDeletionTimestamp,
    )
  } else {
    null
  }

fun Session.toSerializable() =
  SessionSerializable(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expirationTimestamp = expirationTimestamp,
    profileDeletionTimestamp = profileDeletionTimestamp,
  )
