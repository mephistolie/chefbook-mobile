package com.mysty.chefbook.features.recipe.input.ui.mvi

import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.core.android.mvi.MviState

data class RecipeInputScreenState(
    val input: RecipeInput = RecipeInput(),
    val recipeId: String? = null,
    val isLoading: Boolean = false,
) : MviState
