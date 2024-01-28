package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSharedShoppingListRequest(
  @SerialName("shoppingListId")
  val id: String? = null,
  @SerialName("name")
  val name: String? = null,
)


@Serializable
data class CreateSharedShoppingListResponse(
  @SerialName("shoppingListId")
  val id: String,
)
