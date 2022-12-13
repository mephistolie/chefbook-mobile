package com.cactusknights.chefbook.domain.entities.shoppinglist

import java.time.LocalDateTime
import java.time.ZoneOffset

data class ShoppingList(
    val purchases: List<Purchase>,
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
)
