package io.chefbook.sdk.profile.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetProfileDeletionStatusResponse(
  @SerialName("deletionTimestamp")
  val deletionTimestamp: String,
  @SerialName("deleted")
  val deleted: Boolean,
)
