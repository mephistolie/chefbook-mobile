package com.cactusknights.chefbook.screens.main

import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.User
import java.io.Serializable

enum class DashboardFragments : Serializable {
    RECIPES, FAVOURITE, CATEGORIES, SHOPPING_LIST, RECIPES_IN_CATEGORY;

    fun titleId() : Int {
        return when (this) {
            RECIPES -> R.string.recipes
            FAVOURITE -> R.string.favourite
            CATEGORIES -> R.string.categories
            SHOPPING_LIST -> R.string.shopping_list
            RECIPES_IN_CATEGORY -> R.string.category
        }
    }
}

class MainActivityState (
    val currentFragment: DashboardFragments = DashboardFragments.RECIPES,
    val previousFragment: DashboardFragments? = null,
    val user: User? = User(),
    val currentCategory: Category? = null,
    val recipes: ArrayList<Recipe> = arrayListOf(),
    val message: Int? = null
) : Serializable