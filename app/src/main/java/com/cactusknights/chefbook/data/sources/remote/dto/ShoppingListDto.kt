package com.cactusknights.chefbook.data.sources.remote.dto

import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class ShoppingListDto (
    var purchases: ArrayList<PurchaseDto>,
    var timestamp : Date
)

class ShoppingListInputDto (
    var purchases: ArrayList<PurchaseDto>,
)

data class PurchaseDto (
    @SerializedName("purchase_id") var id : String,
    var name: String,
    var multiplier : Int = 1,
    @SerializedName("is_purchased") var isPurchased: Boolean = false
): Serializable {}

fun ShoppingListDto.toShoppingList() : ShoppingList {
    return ShoppingList(
        purchases = this.purchases.map { it.toPurchase() } as ArrayList<Purchase>,
        timestamp = this.timestamp
    )
}

fun ShoppingList.toShoppingListInputDto() : ShoppingListInputDto {
    return ShoppingListInputDto(
        purchases = this.purchases.map { it.toPurchaseDto() } as ArrayList<PurchaseDto>
    )
}

fun PurchaseDto.toPurchase() : Purchase {
    return Purchase(
        id = id,
        name = name,
        multiplier = multiplier,
        isPurchased = isPurchased
    )
}

fun Purchase.toPurchaseDto() : PurchaseDto {
    return PurchaseDto(
        id = id,
        name = name,
        multiplier = multiplier,
        isPurchased = isPurchased
    )
}