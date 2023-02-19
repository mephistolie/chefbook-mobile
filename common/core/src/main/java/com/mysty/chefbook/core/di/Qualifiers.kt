package com.mysty.chefbook.core.di

object Qualifiers {
    const val LOCAL = "local"
    const val REMOTE = "remote"

    const val ANONYMOUS = "anonymous"
    const val AUTHORIZED = "authorized"
    const val ENCRYPTED_IMAGE = "encrypted_image"

    object DataStore {
        const val SETTINGS = "settings"
        const val TOKENS = "tokens"
        const val PROFILE = "profile"
        const val LATEST_RECIPES = "latest_recipes"
        const val SHOPPING_LIST = "shopping_list"
    }
}