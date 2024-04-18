package io.chefbook.sdk.auth.api.internal.data.models

data class Session(
  val accessToken: String,
  val refreshToken: String,
  val expirationTimestamp: String? = null,
  val profileDeletionTimestamp: String? = null,
)
