package com.mysty.chefbook.api.shoppinglist.data.local.mappers

import com.mysty.chefbook.api.common.entities.unit.MeasureUnitMapper
import com.mysty.chefbook.api.shoppinglist.data.local.dto.PurchaseProto
import com.mysty.chefbook.api.shoppinglist.data.local.dto.ShoppingListProto
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList
import com.mysty.chefbook.core.constants.Strings
import java.lang.Integer.max
import java.time.LocalDateTime
import java.time.ZoneOffset


internal fun ShoppingList.toProto(): ShoppingListProto {
    val purchasesProto = purchases.map {
        PurchaseProto.newBuilder()
            .setId(it.id)
            .setName(it.name)
            .setAmount(it.amount ?: 0)
            .setUnit(it.unit?.let { unit -> MeasureUnitMapper.map(unit) } ?: Strings.EMPTY)
            .setMultiplier(it.multiplier)
            .setIsPurchased(it.isPurchased)
            .setRecipeId(it.recipeId ?: Strings.EMPTY)
            .setRecipeName(it.recipeName ?: Strings.EMPTY)
            .build()
    }
    return ShoppingListProto.newBuilder()
        .addAllPurchases(purchasesProto)
        .setTimestamp(timestamp.toEpochSecond(ZoneOffset.UTC))
        .build()
}

internal fun ShoppingListProto.toEntity(): ShoppingList {
    val purchases = this.purchasesList.map {
        Purchase(
            id = it.id,
            name = it.name,
            amount = if (it.amount > 0) it.amount else null,
            unit = if (it.unit.isNotEmpty()) MeasureUnitMapper.map(it.unit) else null,
            multiplier = max(it.multiplier, 1),
            isPurchased = it.isPurchased,
            recipeId = it.recipeId.ifEmpty { null },
            recipeName = it.recipeName.ifEmpty { null },
        )
    }
    return ShoppingList(
        purchases = purchases,
        timestamp = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
    )
}
