package com.cactusknights.chefbook.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

data class ShoppingList (
    var purchases: List<Purchase>,
    var timestamp : Date = Date()
): Serializable {}

data class Purchase (
    @SerializedName("purchase_id") var id : String = UUID.randomUUID().toString(),
    var name: String,
    var multiplier : Int = 1,
    @SerializedName("is_purchased") var isPurchased: Boolean = false
): Serializable {}