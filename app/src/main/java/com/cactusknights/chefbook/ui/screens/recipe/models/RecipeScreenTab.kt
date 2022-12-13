package com.cactusknights.chefbook.ui.screens.recipe.models

import com.cactusknights.chefbook.R

sealed class RecipeScreenTab(
    val id: String,
    val nameId: Int,
) {
    object Details : RecipeScreenTab("DETAILS", R.string.common_general_details)
    object Ingredients : RecipeScreenTab("INGREDIENTS", R.string.common_general_ingredients)
    object Cooking : RecipeScreenTab("COOKING", R.string.common_general_cooking)

    companion object {
        fun byString(string: String?) =
            when(string) {
                Ingredients.id -> Ingredients
                Cooking.id -> Cooking
                else -> Details
            }
    }
}
