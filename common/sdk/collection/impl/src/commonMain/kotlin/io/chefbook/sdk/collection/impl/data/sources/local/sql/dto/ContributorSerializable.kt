package io.chefbook.sdk.collection.impl.data.sources.local.sql.dto

import io.chefbook.libs.models.profile.ProfileInfo
import io.chefbook.sdk.collection.api.external.domain.entities.Contributor
import io.chefbook.sdk.collection.api.internal.data.sources.common.dto.ContributorRoleSerializable
import io.chefbook.sdk.collection.api.internal.data.sources.common.dto.serialize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class ContributorSerializable(
  @SerialName("id")
  val id: String,
  @SerialName("name")
  val name: String? = null,
  @SerialName("avatar")
  val avatar: String? = null,
  @SerialName("role")
  val role: ContributorRoleSerializable,
) {

  fun toEntity(): Contributor =
    Contributor(
      ProfileInfo(
        id = id,
        name = name,
        avatar = avatar,
      ),
      role = role.deserialize(),
    )
}

internal fun Contributor.toSerializable(): ContributorSerializable =
  ContributorSerializable(
    id = id,
    name = name,
    avatar = avatar,
    role = role.serialize(),
  )
