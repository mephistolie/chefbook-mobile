package com.cactusknights.chefbook.domain.entities.shoppinglist

data class PurchaseSection(
    val title: String? = null,
    val purchases: List<Purchase>,
    val recipeId: String? = null,
)
