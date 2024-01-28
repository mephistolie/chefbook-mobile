package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShoppingListsSerializable(
  @SerialName("pendingUploadIds")
  val pendingUploadIds: Set<String> = emptySet(),
)
