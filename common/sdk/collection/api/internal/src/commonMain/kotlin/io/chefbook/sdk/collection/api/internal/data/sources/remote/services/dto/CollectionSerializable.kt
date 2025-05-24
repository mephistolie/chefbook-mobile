package io.chefbook.sdk.collection.api.internal.data.sources.remote.services.dto

import io.chefbook.libs.models.visibility.Visibility
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.network.api.internal.service.dto.responses.ProfileMinInfoSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CollectionSerializable(
  @SerialName("collectionId")
  val collectionId: String,
  @SerialName("name")
  val name: String,
  @SerialName("visibility")
  val visibility: String,
  @SerialName("contributors")
  val contributors: List<ContributorSerializable>,
  @SerialName("recipesCount")
  val recipesCount: Int? = null,
) {

  fun deserialize(
    profilesInfo: Map<String, ProfileMinInfoSerializable>,
  ): Collection =
    Collection(
      id = collectionId,
      name = name,
      visibility = Visibility.deserialize(visibility),
      contributors = contributors.map { contributor ->
        contributor.deserialize(profilesInfo)
      },
      recipesCount = recipesCount ?: 0,
    )
}
