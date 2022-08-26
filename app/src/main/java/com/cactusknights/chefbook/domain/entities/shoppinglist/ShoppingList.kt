package com.cactusknights.chefbook.domain.entities.shoppinglist

import java.time.LocalDateTime

data class ShoppingList(
    val purchases: List<Purchase>,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
