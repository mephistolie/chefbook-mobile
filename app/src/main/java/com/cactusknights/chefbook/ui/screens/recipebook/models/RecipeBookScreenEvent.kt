package com.cactusknights.chefbook.ui.screens.recipebook.models

import com.mysty.chefbook.api.category.domain.entities.CategoryInput

sealed class RecipeBookScreenEvent {
    object OpenRecipeSearch : RecipeBookScreenEvent()
    object OpenFavouriteRecipes : RecipeBookScreenEvent()
    object OpenCommunityRecipes : RecipeBookScreenEvent()
    object OpenEncryptionMenu : RecipeBookScreenEvent()
    object OpenCreateRecipeScreen : RecipeBookScreenEvent()
    data class CreateCategory(val input: CategoryInput) : RecipeBookScreenEvent()
    data class OpenRecipe(val recipeId: String) : RecipeBookScreenEvent()
    data class OpenCategory(val categoryId: String) : RecipeBookScreenEvent()
    object ChangeCategoriesExpanded: RecipeBookScreenEvent()
    data class ChangeNewCategoryDialogVisibility(val isVisible: Boolean): RecipeBookScreenEvent()
}
