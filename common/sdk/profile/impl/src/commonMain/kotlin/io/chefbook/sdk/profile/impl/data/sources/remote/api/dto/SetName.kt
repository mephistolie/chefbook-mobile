package io.chefbook.sdk.profile.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetNameRequest(
  @SerialName("firstName")
  val firstName: String? = null,
  @SerialName("lastName")
  val lastName: String? = null,
) 
