package com.cactusknights.chefbook.data.dto.remote.shoppinglist

import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.Serializable

@Serializable
class ShoppingListResponse (
    var purchases: List<PurchaseBody>,
    var timestamp : String
)



fun ShoppingListResponse.toShoppingList() : ShoppingList {
    return ShoppingList(
        purchases = purchases.toEntity(),
        timestamp = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME),
    )
}
