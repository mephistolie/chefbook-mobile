package io.chefbook.sdk.shoppinglist.impl.data.sources.remote.api.dto

import io.chefbook.sdk.network.api.internal.service.dto.responses.ProfileInfoSerializable
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingListMeta
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.PurchaseSerializable
import io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto.ShoppingListTypeSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShoppingListSerializable(
  @SerialName("shoppingListId")
  val id: String,
  @SerialName("name")
  val name: String? = null,
  @SerialName("type")
  val type: ShoppingListTypeSerializable,
  @SerialName("owner")
  val owner: ProfileInfoSerializable,
  @SerialName("purchases")
  val purchases: List<PurchaseSerializable>,
  @SerialName("recipeNames")
  val recipeNames: Map<String, String>?,
  @SerialName("version")
  val version: Int,
) {

  fun toEntity(): ShoppingList =
    ShoppingList(
      meta = ShoppingListMeta(
        id = id,
        name = name,
        type = type.toEntity(),
        owner = owner.toEntity(),
        version = version,
      ),
      purchases = purchases.map(PurchaseSerializable::toEntity),
      recipeNames = recipeNames.orEmpty(),
    )
}
