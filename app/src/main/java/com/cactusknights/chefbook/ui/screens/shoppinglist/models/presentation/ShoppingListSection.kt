package com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation

import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase

data class ShoppingListSection(
    val purchases: List<Purchase>,
    val title: String? = null,
    val recipeId: String? = null,
)
