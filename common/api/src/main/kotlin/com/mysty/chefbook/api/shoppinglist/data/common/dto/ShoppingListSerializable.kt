package com.mysty.chefbook.api.shoppinglist.data.common.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList
import com.mysty.chefbook.core.parseTimestampSafely
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ShoppingListSerializable(
    @SerialName("purchases")
    val purchases: List<PurchaseSerializable>,
    @SerialName("timestamp")
    val timestamp: String,
) {
    fun toEntity(): ShoppingList =
        ShoppingList(
            purchases = purchases.map(PurchaseSerializable::toEntity),
            timestamp = parseTimestampSafely(timestamp),
        )
}
