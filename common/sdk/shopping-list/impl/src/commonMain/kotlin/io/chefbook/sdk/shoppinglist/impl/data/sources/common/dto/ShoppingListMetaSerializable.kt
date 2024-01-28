package io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto

import io.chefbook.sdk.network.api.internal.service.dto.responses.ProfileInfoSerializable
import io.chefbook.sdk.network.api.internal.service.dto.responses.toSerializable
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShoppingListMetaSerializable(
  @SerialName("shoppingListId")
  val id: String,
  @SerialName("name")
  val name: String? = null,
  @SerialName("type")
  val type: ShoppingListTypeSerializable,
  @SerialName("owner")
  val owner: ProfileInfoSerializable,
  @SerialName("version")
  val version: Int,
) {

  fun toEntity() = ShoppingListMeta(
    id = id,
    name = name,
    type = type.toEntity(),
    owner = owner.toEntity(),
    version = version,
  )
}


internal fun ShoppingListMeta.toSerializable() = ShoppingListMetaSerializable(
  id = id,
  name = name,
  type = type.toSerializable(),
  owner = owner.toSerializable(),
  version = version,
)
