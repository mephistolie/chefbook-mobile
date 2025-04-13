package io.chefbook.sdk.profile.impl.data.sources.common.dto

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.external.domain.entities.SubscriptionPlan
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileSerializable(
  @SerialName("profileId")
  val id: String,
  @SerialName("nickname")
  val nickname: String? = null,
  @SerialName("email")
  val email: String? = null,
  @SerialName("role")
  val role: Role = Role.MEMBER,
  @SerialName("oAuth")
  val oAuth: OAuth? = null,
  @SerialName("blocked")
  val isBlocked: Boolean = false,
  @SerialName("registrationTimestamp")
  val registrationTimestamp: String? = null,
  @SerialName("firstName")
  val firstName: String? = null,
  @SerialName("lastName")
  val lastName: String? = null,
  @SerialName("description")
  val description: String? = null,
  @SerialName("avatar")
  val avatar: String? = null,
  @SerialName("subscriptionPlan")
  val subscriptionPlan: String = "free",
  @SerialName("broccoins")
  val broccoins: Int = 0,
) {

  fun toEntity() = Profile(
    id = id,
    nickname = nickname,
    email = email,
    role = when (role) {
      Role.MEMBER -> Profile.Role.MEMBER
      Role.ADMIN -> Profile.Role.ADMIN
    },
    oAuth = Profile.OAuth(
      googleId = oAuth?.googleId,
      vkId = oAuth?.vkId,
    ),
    isBlocked = isBlocked,
    registrationTimestamp = registrationTimestamp,
    firstName = firstName,
    lastName = lastName,
    description = description,
    avatar = avatar,
    subscriptionPlan = when (subscriptionPlan) {
      "free" -> SubscriptionPlan.FREE
      "premium" -> SubscriptionPlan.PREMIUM
      "maximum" -> SubscriptionPlan.MAXIMUM
      else -> SubscriptionPlan.PREMIUM
    },
    broccoins = broccoins,
  )

  @Serializable
  internal data class OAuth(
    @SerialName("googleId")
    val googleId: String?,
    @SerialName("vkId")
    val vkId: Long? = null,
  )

  @Serializable
  internal enum class Role {

    @SerialName("member")
    MEMBER,

    @SerialName("admin")
    ADMIN,
  }
}

internal fun Profile.toSerializable() =
  ProfileSerializable(
    id = id,
    nickname = nickname,
    email = email,
    role = when (role) {
      Profile.Role.MEMBER -> ProfileSerializable.Role.MEMBER
      Profile.Role.ADMIN -> ProfileSerializable.Role.ADMIN
    },
    oAuth = oAuth?.let { oAuth ->
      ProfileSerializable.OAuth(
        googleId = oAuth.googleId,
        vkId = oAuth.vkId,
      )
    },
    isBlocked = isBlocked,
    registrationTimestamp = registrationTimestamp,
    firstName = firstName,
    lastName = lastName,
    avatar = avatar,
    description = description,
    subscriptionPlan = when (subscriptionPlan) {
      SubscriptionPlan.FREE -> "free"
      SubscriptionPlan.PREMIUM -> "premium"
      SubscriptionPlan.MAXIMUM -> "maximum"
    },
    broccoins = broccoins,
  )
