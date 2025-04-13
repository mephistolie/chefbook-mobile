package io.chefbook.sdk.collection.api.external.domain.entities

import io.chefbook.libs.models.profile.ProfileInfo

data class Contributor(
  private val profile: ProfileInfo,
  val role: Role
) {

  val id by profile::id
  val name by profile::name
  val avatar by profile::avatar

  enum class Role {
    OWNER,
    COAUTHOR;

    fun serialize(): String =
      when (this) {
        OWNER -> ROLE_OWNER
        COAUTHOR -> ROLE_COAUTHOR
      }

    companion object {
      fun deserialize(role: String?): Role =
        when (role) {
          ROLE_OWNER -> OWNER
          else -> COAUTHOR
        }
    }
  }

  companion object {
    private const val ROLE_OWNER = "owner"
    private const val ROLE_COAUTHOR = "coauthor"
  }
}
