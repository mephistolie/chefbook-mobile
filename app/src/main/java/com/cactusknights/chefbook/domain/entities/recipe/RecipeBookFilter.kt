package com.cactusknights.chefbook.domain.entities.recipe

import com.cactusknights.chefbook.domain.entities.common.Sorting
import java.io.Serializable

data class RecipeBookFilter(
    val search: String? = null,
    val sortBy: Sorting = Sorting.NAME,
    val onlyFavourite: Boolean = false,
    val targetCategoryId: Int? = null,
) : Serializable
