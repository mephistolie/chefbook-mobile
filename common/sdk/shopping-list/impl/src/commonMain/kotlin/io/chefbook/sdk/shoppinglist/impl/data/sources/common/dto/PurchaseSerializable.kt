package io.chefbook.sdk.shoppinglist.impl.data.sources.common.dto

import io.chefbook.common.models.measureunit.MeasureUnitMapper
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PurchaseSerializable(
  @SerialName("purchaseId")
  val id: String,
  @SerialName("name")
  val name: String,
  @SerialName("multiplier")
  val multiplier: Int? = null,
  @SerialName("purchased")
  val isPurchased: Boolean? = null,
  @SerialName("amount")
  val amount: Int? = null,
  @SerialName("measureUnit")
  val measureUnit: String? = null,
  @SerialName("recipeId")
  val recipeId: String? = null,
) {
  fun toEntity() = Purchase(
    id = id,
    name = name,
    amount = amount,
    measureUnit = MeasureUnitMapper.map(measureUnit),
    multiplier = multiplier ?: 1,
    isPurchased = isPurchased ?: false,
    recipeId = recipeId,
  )
}

internal fun Purchase.toSerializable() = PurchaseSerializable(
  id = id,
  name = name,
  amount = amount,
  measureUnit = MeasureUnitMapper.map(measureUnit),
  multiplier = multiplier,
  isPurchased = isPurchased,
  recipeId = recipeId,
)
