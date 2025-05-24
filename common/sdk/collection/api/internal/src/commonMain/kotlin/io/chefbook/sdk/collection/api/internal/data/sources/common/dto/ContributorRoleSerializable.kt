package io.chefbook.sdk.collection.api.internal.data.sources.common.dto

import io.chefbook.sdk.collection.api.external.domain.entities.Contributor
import io.chefbook.sdk.collection.api.internal.data.sources.common.dto.ContributorRoleSerializable.COAUTHOR
import io.chefbook.sdk.collection.api.internal.data.sources.common.dto.ContributorRoleSerializable.OWNER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ContributorRoleSerializable {

  @SerialName("owner")
  OWNER,

  @SerialName("coauthor")
  COAUTHOR;

  fun deserialize(): Contributor.Role = when (this) {
    OWNER -> Contributor.Role.OWNER
    COAUTHOR -> Contributor.Role.COAUTHOR
  }
}

fun Contributor.Role.serialize(): ContributorRoleSerializable = when (this) {
  Contributor.Role.OWNER -> OWNER
  Contributor.Role.COAUTHOR -> COAUTHOR
}
