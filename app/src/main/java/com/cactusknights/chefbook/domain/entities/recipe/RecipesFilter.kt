package com.cactusknights.chefbook.domain.entities.recipe

import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.Sorting

data class RecipesFilter(
    val search: String? = null,
    val owned: Boolean = false,
    val saved: Boolean = false,
    val authorId: Int? = null,
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
