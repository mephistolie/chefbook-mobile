package io.chefbook.sdk.shoppinglist.impl.data.sources.local.datastore.dto

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.PurchaseSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListMetaSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.toSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShoppingListSerializable(
  @SerialName("meta")
  val meta: ShoppingListMetaSerializable,
  @SerialName("purchases")
  val purchases: List<PurchaseSerializable>,
  @SerialName("recipeNames")
  val recipeNames: Map<String, String>,
) {

  fun toEntity() =
    ShoppingList(
      meta = meta.toEntity(),
      purchases = purchases.map(PurchaseSerializable::toEntity),
      recipeNames = recipeNames,
    )
}

internal fun ShoppingList.toSerializable() =
  ShoppingListSerializable(
    meta = meta.toSerializable(),
    purchases = purchases.map(Purchase::toSerializable),
    recipeNames = recipeNames,
  )
