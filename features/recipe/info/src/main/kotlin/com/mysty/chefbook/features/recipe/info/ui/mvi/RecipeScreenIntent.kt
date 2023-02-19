package com.mysty.chefbook.features.recipe.info.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent
import com.mysty.chefbook.core.constants.Strings

internal sealed class RecipeScreenIntent : MviIntent {
    object ReloadRecipe : RecipeScreenIntent()
    object ChangeLikeStatus : RecipeScreenIntent()
    object AddToRecipeBook : RecipeScreenIntent()
    object OpenRecipeMenu : RecipeScreenIntent()
    object OpenRecipeDetails : RecipeScreenIntent()

    data class ChangeIngredientSelectedStatus(val ingredientId: String) : RecipeScreenIntent()
    data class ChangeServings(val offset: Int) : RecipeScreenIntent()
    object AddSelectedIngredientsToShoppingList : RecipeScreenIntent()

    object OpenShareDialog : RecipeScreenIntent()
    data class OpenPicturesViewer(val selectedPicture: String = Strings.EMPTY) : RecipeScreenIntent()

    object Close : RecipeScreenIntent()
}
