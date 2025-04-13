package io.chefbook.sdk.network.api.internal.service.dto.responses

import io.chefbook.libs.models.profile.ProfileInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileMinInfoSerializable(
  @SerialName("name")
  val name: String? = null,
  @SerialName("avatar")
  val avatar: String? = null,
) {

  fun deserialize(id: String) =
    ProfileInfo(
      id = id,
      name = name,
      avatar = avatar,
    )
}
