package io.chefbook.sdk.profile.api.external.domain.entities

import io.chefbook.libs.models.auth.LOCAL_PROFILE_ID
import io.chefbook.libs.models.profile.ProfileInfo

data class Profile(
  val id: String,
  val nickname: String? = null,
  val email: String? = null,
  val role: Role = Role.MEMBER,
  val oAuth: OAuth? = null,
  val isBlocked: Boolean = false,
  val registrationTimestamp: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val description: String? = null,
  val avatar: String? = null,
  val subscriptionPlan: SubscriptionPlan = SubscriptionPlan.FREE,
  val broccoins: Int = 0,
) {

  val isOnline by lazy { id == LOCAL_PROFILE_ID }

  val username: String =
    when {
      firstName != null || lastName != null -> (firstName?.let { "$it " } + lastName).trim()
      nickname != null -> nickname
      email != null -> email
      else -> "#${id.substringBefore("-")}"
    }

  val info: ProfileInfo
    get() = ProfileInfo(
      id = id,
      name = username,
      avatar = avatar,
    )

  enum class Role {
    MEMBER, ADMIN
  }

  data class OAuth(
    val googleId: String? = null,
    val vkId: Long? = null,
  )

  companion object {
    val local = Profile(id = LOCAL_PROFILE_ID)
  }
}
