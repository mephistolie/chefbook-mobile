package com.mysty.chefbook.api.recipe.domain.entities

import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.recipe.domain.entities.sorting.Sorting

data class RecipesFilter(
    val search: String? = null,
    val owned: Boolean = false,
    val saved: Boolean = false,
    val authorId: String? = null,
    val sortBy: Sorting? = null,
    val languages: List<Language>? = null,
    val minTime: Int? = null,
    val maxTime: Int? = null,
    val minServings: Int? = null,
    val maxServings: Int? = null,
    val minCalories: Int? = null,
    val maxCalories: Int? = null,
    val page: Int? = null,
    val pageSize: Int? = null,
)
