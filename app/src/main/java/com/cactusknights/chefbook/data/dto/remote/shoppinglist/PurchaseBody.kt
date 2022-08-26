package com.cactusknights.chefbook.data.dto.remote.shoppinglist

import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseBody (
    @SerialName("purchase_id")
    val id : String,
    @SerialName("name")
    var name: String,
    @SerialName("multiplier")
    var multiplier : Int = 1,
    @SerialName("is_purchased")
    val isPurchased: Boolean = false
)

fun PurchaseBody.toEntity() : Purchase {
    return Purchase(
        id = id,
        name = name,
        multiplier = multiplier,
        isPurchased = isPurchased
    )
}

fun Purchase.toBody() : PurchaseBody {
    return PurchaseBody(
        id = id,
        name = name,
        multiplier = multiplier,
        isPurchased = isPurchased
    )
}

fun List<PurchaseBody>.toEntity() : List<Purchase> =
    this.map { it.toEntity() }


fun List<Purchase>.toBody() : List<PurchaseBody> =
    this.map { it.toBody() }
