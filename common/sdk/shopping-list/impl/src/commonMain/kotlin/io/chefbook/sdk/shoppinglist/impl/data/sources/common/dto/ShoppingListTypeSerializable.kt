package io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal enum class ShoppingListTypeSerializable {
  @SerialName("personal")
  PERSONAL,

  @SerialName("shared")
  SHARED;

  fun toEntity() = when (this) {
    PERSONAL -> ShoppingListMeta.Type.PERSONAL
    SHARED -> ShoppingListMeta.Type.SHARED
  }
}

internal fun ShoppingListMeta.Type.toSerializable() = when (this) {
  ShoppingListMeta.Type.PERSONAL -> ShoppingListTypeSerializable.PERSONAL
  ShoppingListMeta.Type.SHARED -> ShoppingListTypeSerializable.SHARED
}
