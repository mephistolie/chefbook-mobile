package com.mysty.chefbook.features.recipe.control.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class RecipeControlScreenIntent : MviIntent {
    object ChangeFavouriteStatus : RecipeControlScreenIntent()
    object ChangeSavedStatus : RecipeControlScreenIntent()
    object ChangeCategories : RecipeControlScreenIntent()
    object EditRecipe : RecipeControlScreenIntent()
    object DeleteRecipe : RecipeControlScreenIntent()

    object OpenRemoveFromRecipeBookDialog : RecipeControlScreenIntent()
    object OpenDeleteDialog : RecipeControlScreenIntent()

    object Close : RecipeControlScreenIntent()
}
