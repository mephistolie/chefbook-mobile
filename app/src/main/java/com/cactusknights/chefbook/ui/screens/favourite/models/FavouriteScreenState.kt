package com.cactusknights.chefbook.ui.screens.favourite.models

import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo

data class FavouriteScreenState(
    val recipes: List<RecipeInfo>? = null,
)
