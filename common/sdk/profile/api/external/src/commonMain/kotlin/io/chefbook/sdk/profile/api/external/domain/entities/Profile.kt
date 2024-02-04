package io.chefbook.sdk.profile.api.external.domain.entities

data class Profile(
  val id: String,
  val email: String? = null,
  val nickname: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val description: String? = null,
  val creationTimestamp: String? = null,
  val avatar: String? = null,
  val subscriptionPlan: SubscriptionPlan = SubscriptionPlan.FREE,
  val broccoins: Int = 0,
  val isOnline: Boolean = false,
) {

  val username: String =
    when {
      firstName != null || lastName != null -> (firstName?.let { "$it " } + lastName).trim()
      nickname != null -> nickname
      email != null -> email
      else -> "#${id.substringBefore("-")}"
    }

  companion object {
    const val LOCAL_PROFILE_ID = "LOCAL_PROFILE"

    val local = Profile(id = LOCAL_PROFILE_ID)
  }
}
