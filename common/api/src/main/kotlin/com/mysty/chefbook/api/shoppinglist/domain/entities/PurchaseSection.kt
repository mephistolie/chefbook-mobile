package com.mysty.chefbook.api.shoppinglist.domain.entities

data class PurchaseSection(
    val title: String? = null,
    val purchases: List<Purchase>,
    val recipeId: String? = null,
)
