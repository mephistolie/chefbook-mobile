package com.cactusknights.chefbook.domain.entities.settings

data class Settings(
    val mode: Mode,
    val icon: Icon,
    val theme: Theme,
    val defaultTab: Tab,
    val isFirstLaunch: Boolean,
    val defaultRecipeLanguage: String,
    val onlineRecipeLanguages: List<String>,
)

enum class Mode {
    OFFLINE, ONLINE
}

enum class Icon {
    STANDARD, OLD
}

enum class Theme {
    SYSTEM, LIGHT, DARK
}

enum class Tab {
    RECIPE_BOOK, SHOPPING_LIST
}