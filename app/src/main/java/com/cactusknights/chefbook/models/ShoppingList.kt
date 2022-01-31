package com.cactusknights.chefbook.models

import com.cactusknights.chefbook.PurchaseProto
import com.cactusknights.chefbook.ShoppingListProto
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class ShoppingList(
    var purchases: List<Purchase>,
    var timestamp: Date = Date()
) : Serializable {}

data class Purchase(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var multiplier: Int = 1,
    var isPurchased: Boolean = false
) : Serializable {}


fun ShoppingList.toProto(): ShoppingListProto {
    val purchasesProto = this.purchases.map {
        PurchaseProto.newBuilder()
            .setId(it.id)
            .setName(it.name)
            .setMultiplier(it.multiplier)
            .setIsPurchased(it.isPurchased)
            .build()
    }
    return ShoppingListProto.newBuilder()
        .addAllPurchases(purchasesProto)
        .setTimestamp(this.timestamp.time)
        .build()
}

fun ShoppingListProto.toShoppingList(): ShoppingList {
    val purchases = this.purchasesList.map {
        Purchase(
            id = it.id,
            name = it.name,
            multiplier = it.multiplier,
            isPurchased = it.isPurchased
        )
    }
    return ShoppingList(
        purchases = purchases,
        timestamp = Date(this.timestamp)
    )
}

fun Selectable<String>.toPurchase() : Purchase {
    return Purchase(name = this.item?:"")
}