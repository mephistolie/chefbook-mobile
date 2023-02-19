package com.mysty.chefbook.features.recipebook.favourite.ui.mvi

import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.mvi.MviState

internal data class FavouriteRecipesScreenState(
    val recipes: List<RecipeInfo>? = null,
) : MviState
