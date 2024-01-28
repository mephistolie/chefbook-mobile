package io.chefbook.sdk.profile.impl.data.sources.remote.dto

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetProfileResponse(
  @SerialName("profileId")
  val id: String,
  @SerialName("nickname")
  val nickname: String? = null,
  @SerialName("email")
  val email: String? = null,
  @SerialName("role")
  val role: String? = null,
  @SerialName("oAuth")
  val oAuth: OAuh? = null,
  @SerialName("blocked")
  val isBlocked: Boolean? = false,
  @SerialName("registeredAt")
  val registrationTimestamp: String? = null,
  @SerialName("firstName")
  val firstName: String? = null,
  @SerialName("lastName")
  val lastName: String? = null,
  @SerialName("description")
  val description: String? = null,
  @SerialName("avatar")
  val avatar: String? = null,
//    @SerialName("premium")
//    val premium: Boolean = false,
//    @SerialName("broccoins")
//    val broccoins: Int = 0,
) {

  // TODO
  fun toEntity() = Profile(
    id = id,
    email = email,
    username = "$firstName $lastName",
    creationTimestamp = registrationTimestamp,
    premium = true,
    avatar = avatar,
    broccoins = 0,
    isOnline = true,
  )
}

@Serializable
internal data class OAuh(
  @SerialName("googleId")
  val googleId: String?,
  @SerialName("vkId")
  val vkId: Long? = null,
)
