package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSharedShoppingListLinkResponse(
  @SerialName("link")
  val link: String,
  @SerialName("expiresAt")
  val expirationTimestamp: String,
)
