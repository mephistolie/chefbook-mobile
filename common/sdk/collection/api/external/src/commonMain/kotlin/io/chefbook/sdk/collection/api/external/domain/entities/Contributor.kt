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
    COAUTHOR,
  }
}
