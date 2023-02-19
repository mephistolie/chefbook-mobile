package com.mysty.chefbook.features.recipe.control.ui.mvi

import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.mvi.MviState

internal data class RecipeControlScreenState(
    val recipe: RecipeInfo? = null,
) : MviState
