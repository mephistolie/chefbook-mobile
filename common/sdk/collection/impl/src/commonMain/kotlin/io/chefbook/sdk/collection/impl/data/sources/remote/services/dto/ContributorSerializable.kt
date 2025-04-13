package io.chefbook.sdk.collection.impl.data.sources.remote.services.dto

import io.chefbook.sdk.collection.api.external.domain.entities.Contributor
import io.chefbook.sdk.network.api.internal.service.dto.responses.ProfileMinInfoSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class ContributorSerializable(
  @SerialName("contributorId")
  val contributorId: String,
  @SerialName("role")
  val role: String,
) {

  fun deserialize(
    profilesInfo: Map<String, ProfileMinInfoSerializable>,
  ): Contributor {
    val profile = profilesInfo[contributorId] ?: ProfileMinInfoSerializable()
    return Contributor(
      profile = profile.deserialize(contributorId),
      role = Contributor.Role.deserialize(role),
    )
  }
}
