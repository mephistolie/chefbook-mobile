package com.mysty.chefbook.features.shoppinglist.control.ui.screen.presentation

import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase

internal data class ShoppingListSection(
    val purchases: List<Purchase>,
    val title: String? = null,
    val recipeId: String? = null,
)
