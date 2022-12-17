package com.cactusknights.chefbook.ui.screens.recipe.models

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.recipe.domain.entities.Recipe

sealed class RecipeScreenState {
    object Loading : RecipeScreenState()

    data class Success(
        val recipe: Recipe,
        val categoriesForSelection: List<Category>? = null,

        val isShareDialogVisible: Boolean = false,
        val picturesDialogState: RecipePicturesDialogState = RecipePicturesDialogState.Hidden,
        val isRemoveFromRecipeBookDialogVisible: Boolean = false,
        val isDeleteRecipeDialogVisible: Boolean = false,
    ) : RecipeScreenState()

    class Error(val error: Throwable?) : RecipeScreenState()
}

sealed class RecipePicturesDialogState {
    object Hidden : RecipePicturesDialogState()
    data class Visible(
        val pictures: List<String>,
        val startIndex: Int = 0,
    ) : RecipePicturesDialogState()
}
