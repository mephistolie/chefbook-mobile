package com.cactusknights.chefbook.ui.screens.recipe.models

import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem

sealed class RecipeScreenEvent {
    data class LoadRecipe(val recipeId: String) : RecipeScreenEvent()
    object ChangeLikeStatus : RecipeScreenEvent()
    object ChangeSavedStatus : RecipeScreenEvent()
    object ChangeFavouriteStatus : RecipeScreenEvent()
    data class OpenCategoryScreen(val categoryId: String) : RecipeScreenEvent()
    object ChangeCategories : RecipeScreenEvent()
    object DiscardCategoriesChanging : RecipeScreenEvent()
    data class ConfirmCategoriesChanging(val categories: List<String>) : RecipeScreenEvent()
    data class AddIngredientsToShoppingList(val ingredients: List<IngredientItem>, val multiplier: Float) : RecipeScreenEvent()
    object EditRecipe : RecipeScreenEvent()
    object DeleteRecipe : RecipeScreenEvent()

    sealed class ChangeDialogState(open val isVisible: Boolean) : RecipeScreenEvent() {
        data class Share(override val isVisible: Boolean) : ChangeDialogState(isVisible)
        data class Pictures(override val isVisible: Boolean, val selectedPicture: String = "") : ChangeDialogState(isVisible)
        data class RemoveFromRecipeBook(override val isVisible: Boolean) : ChangeDialogState(isVisible)
        data class Delete(override val isVisible: Boolean) : ChangeDialogState(isVisible)
    }

}
