package com.cactusknights.chefbook.core

import com.cactusknights.chefbook.ProfileProto
import com.cactusknights.chefbook.PurchaseProto
import com.cactusknights.chefbook.ShoppingListProto
import com.mysty.chefbook.core.constants.Strings
import com.cactusknights.chefbook.core.mappers.MeasureUnitMapper
import com.cactusknights.chefbook.domain.entities.profile.Profile
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList
import java.lang.Integer.max
import java.time.LocalDateTime
import java.time.ZoneOffset

fun ShoppingList.toProto(): ShoppingListProto {
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

fun ShoppingListProto.toEntity(): ShoppingList {
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

fun Profile.toProto(): ProfileProto {
    return ProfileProto.newBuilder()
        .setId(id)
        .setEmail(email)
        .setUsername(username)
        .setCreationDate(creationDate.toEpochSecond(ZoneOffset.UTC))
        .setAvatar(avatar ?: "")
        .setPremium(premium)
        .setBroccoins(broccoins)
        .build()
}

fun ProfileProto.toEntity(): Profile {
    return Profile(
        id = id,
        email = email,
        username = username,
        creationDate = LocalDateTime.ofEpochSecond(creationDate, 0, ZoneOffset.UTC),
        avatar = avatar.ifEmpty { null },
        premium = premium,
        broccoins = broccoins,
        isOnline = true,
    )
}
