package io.chefbook.sdk.auth.impl.data.sources.local.datastore.dto

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokensSerializable(
  @SerialName("accessToken")
  val accessToken: String? = null,
  @SerialName("refreshToken")
  val refreshToken: String? = null,
)

fun TokensSerializable.toModel() =
  if (accessToken != null && refreshToken != null) BearerTokens(accessToken, refreshToken) else null

fun BearerTokens.toSerializable() =
  TokensSerializable(accessToken, refreshToken)
