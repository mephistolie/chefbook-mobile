package io.chefbook.sdk.auth.impl.data.sources.remote.services.sessions.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SessionResponse(
  @SerialName("id")
  val id: Long,
  @SerialName("current")
  val isCurrent: Boolean,
  @SerialName("ip")
  val ip: String,
  @SerialName("accessPoint")
  val accessPoint: String,
  @SerialName("isMobile")
  val isMobile: Boolean,
  @SerialName("accessTime")
  val accessTime: String,
  @SerialName("location")
  val location: String,
)
