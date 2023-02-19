package com.mysty.chefbook.features.recipebook.dashboard.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class RecipeBookScreenIntent : MviIntent {
    object OpenRecipeSearch : RecipeBookScreenIntent()
    object OpenFavouriteRecipes : RecipeBookScreenIntent()
    object OpenCommunityRecipes : RecipeBookScreenIntent()
    object OpenEncryptionMenu : RecipeBookScreenIntent()
    object OpenCreateRecipeScreen : RecipeBookScreenIntent()
    data class OpenRecipe(val recipeId: String) : RecipeBookScreenIntent()
    data class OpenCategory(val categoryId: String) : RecipeBookScreenIntent()
    object ChangeCategoriesExpanded: RecipeBookScreenIntent()
    data class ChangeNewCategoryDialogVisibility(val isVisible: Boolean): RecipeBookScreenIntent()
}
