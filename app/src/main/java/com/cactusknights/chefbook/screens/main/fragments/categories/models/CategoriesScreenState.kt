package com.cactusknights.chefbook.screens.main.fragments.categories.models

import com.cactusknights.chefbook.models.Category

sealed class CategoriesScreenState {
    object Loading : CategoriesScreenState()
    data class CategoriesUpdated(val categories: List<Category> = arrayListOf()) : CategoriesScreenState()
}