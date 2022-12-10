package com.cactusknights.chefbook.domain.entities.shoppinglist

data class Purchase(
    val id: String,
    val name: String,
    val multiplier: Int = 1,
    val isPurchased: Boolean = false,
    val recipeId: Int? = null,
    val recipeName: String? = null,
)
