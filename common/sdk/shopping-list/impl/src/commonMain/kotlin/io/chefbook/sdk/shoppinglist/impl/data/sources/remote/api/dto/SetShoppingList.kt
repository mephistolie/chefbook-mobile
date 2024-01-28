package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto

import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.PurchaseSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetShoppingListRequest(
  @SerialName("purchases")
  val purchases: List<PurchaseSerializable>,
  @SerialName("lastVersion")
  val lastVersion: Int? = null,
)
