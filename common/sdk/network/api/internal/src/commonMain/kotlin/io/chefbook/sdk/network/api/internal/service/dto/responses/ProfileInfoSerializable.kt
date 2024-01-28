package io.chefbook.sdk.network.api.internal.service.dto.responses

import io.chefbook.libs.models.profile.ProfileInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfoSerializable(
  @SerialName("id")
  val id: String,
  @SerialName("name")
  val name: String? = null,
  @SerialName("avatar")
  val avatar: String? = null,
) {

  fun toEntity() =
    ProfileInfo(
      id = id,
      name = name,
      avatar = avatar,
    )
}

fun ProfileInfo.toSerializable() =
  ProfileInfoSerializable(
    id = id,
    name = name,
    avatar = avatar,
  )
