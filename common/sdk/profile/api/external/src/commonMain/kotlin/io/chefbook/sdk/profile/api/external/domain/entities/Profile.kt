package io.chefbook.sdk.profile.api.external.domain.entities

data class Profile(
  val id: String,
  val email: String? = null,
  val username: String? = null,
  val creationTimestamp: String? = null,
  val avatar: String? = null,
  val premium: Boolean = false,
  val broccoins: Int = 0,
  val isOnline: Boolean = false,
) {

  companion object {
    const val LOCAL_PROFILE_ID = "LOCAL_PROFILE"

    val local = Profile(id = LOCAL_PROFILE_ID)
  }
}
