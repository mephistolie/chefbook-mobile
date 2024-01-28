package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetShoppingListNameRequest(
  @SerialName("name")
  val name: String? = null,
)
