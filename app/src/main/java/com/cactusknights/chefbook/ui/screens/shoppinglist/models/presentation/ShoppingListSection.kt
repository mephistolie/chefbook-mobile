package com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation

import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase

data class ShoppingListSection(
    val purchases: List<Purchase>,
    val title: String? = null,
    val recipeId: String? = null,
)
