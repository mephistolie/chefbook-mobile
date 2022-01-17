package com.cactusknights.chefbook.screens.main.fragments.categories.models

import com.cactusknights.chefbook.models.Category

sealed class CategoriesState {
    object Loading : CategoriesState()
    data class CategoriesUpdated(val categories: List<Category> = arrayListOf()) : CategoriesState()
}