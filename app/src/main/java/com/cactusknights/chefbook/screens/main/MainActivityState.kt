package com.cactusknights.chefbook.screens.main

enum class DashboardFragments {
    RECIPES, FAVOURITE, CATEGORIES, SHOPPING_LIST
}

class MainActivityState (
    currentFragment: DashboardFragments = DashboardFragments.RECIPES,
)