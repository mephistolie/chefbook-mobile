package com.cactusknights.chefbook.ui.screens.recipeinput.models

import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput

data class RecipeInputScreenState(
    val input: RecipeInput = RecipeInput(),
    val recipeId: String? = null,
    val isCancelDialogOpen: Boolean = false,
    val isLoadingDialogOpen: Boolean = false,
    val isRecipeSavedDialogOpen: Boolean = false,
)
