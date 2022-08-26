package com.cactusknights.chefbook.ui.screens.favourite.models

import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo

data class FavouriteScreenState(
    val recipes: List<RecipeInfo>? = null,
)
