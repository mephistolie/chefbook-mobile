package com.mysty.chefbook.features.recipe.control.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class RecipeControlScreenEffect : MviSideEffect {
    object OpenRemoveFromRecipeBookConfirmDialog : RecipeControlScreenEffect()
    object OpenCategoriesSelectionPage : RecipeControlScreenEffect()
    data class EditRecipe(val recipeId: String) : RecipeControlScreenEffect()
    object OpenDeleteRecipeConfirmDialog : RecipeControlScreenEffect()

    data class ShowToast(val messageId: Int) : RecipeControlScreenEffect()
    object Close : RecipeControlScreenEffect()
}
