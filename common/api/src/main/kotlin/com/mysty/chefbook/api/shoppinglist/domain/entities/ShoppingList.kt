package com.mysty.chefbook.api.shoppinglist.domain.entities

import java.time.LocalDateTime
import java.time.ZoneOffset

data class ShoppingList(
    val purchases: List<Purchase>,
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
)
