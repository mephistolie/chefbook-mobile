package com.mysty.chefbook.api.recipe.domain.entities

import com.mysty.chefbook.api.recipe.domain.entities.sorting.Sorting

data class RecipeBookFilter(
    val search: String? = null,
    val sortBy: Sorting = Sorting.NAME,
    val onlyFavourite: Boolean = false,
    val targetCategoryId: String? = null,
)
