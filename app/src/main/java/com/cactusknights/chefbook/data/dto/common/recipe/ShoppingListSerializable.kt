package com.cactusknights.chefbook.data.dto.common.recipe

import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import com.mysty.chefbook.core.parseTimestampSafely
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListSerializable(
    @SerialName("purchases")
    val purchases: List<PurchaseSerializable>,
    @SerialName("timestamp")
    val timestamp: String,
)

fun ShoppingListSerializable.toEntity(): ShoppingList =
    ShoppingList(
        purchases = purchases.map(PurchaseSerializable::toEntity),
        timestamp = parseTimestampSafely(timestamp),
    )
