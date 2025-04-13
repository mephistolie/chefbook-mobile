package io.chefbook.sdk.auth.impl.data.sources.local.dto

import io.chefbook.sdk.auth.impl.data.sources.common.dto.SessionSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SessionsInfo(
  @SerialName("sessions")
  val sessions: Map<String, SessionSerializable> = emptyMap(),
  @SerialName("profileId")
  val currentProfileId: String? = null,
)
