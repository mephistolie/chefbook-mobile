package com.cactusknights.chefbook.legacy.ui

import androidx.annotation.StringRes
import com.cactusknights.chefbook.R

sealed class Tab(
    val route: String,
    @StringRes val title: Int,
    val iconId: Int
) {
    object ShoppingList : Tab("shopping_list", R.string.shopping_list, R.drawable.ic_list)
    object Recipes : Tab("recipes", R.string.recipes, R.drawable.ic_recipes)
}