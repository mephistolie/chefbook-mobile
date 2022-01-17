package com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models

import com.cactusknights.chefbook.models.ShoppingList

data class ShoppingListState (
    val shoppingList: ShoppingList = ShoppingList(listOf())
)