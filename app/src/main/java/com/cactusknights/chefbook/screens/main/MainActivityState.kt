package com.cactusknights.chefbook.screens.main

enum class DashboardFragments {
    RECIPES, FAVOURITE, CATEGORIES, SHOPPING_LIST
}

class MainActivityState (
    val currentFragment: DashboardFragments = DashboardFragments.RECIPES,
    val isLoggedIn: Boolean = true
)