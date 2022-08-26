package com.cactusknights.chefbook.ui.screens.recipe.models

sealed class RecipeScreenEvent {
    data class LoadRecipe(val recipeId: Int) : RecipeScreenEvent()
    object ChangeLikeStatus : RecipeScreenEvent()
    object ChangeSavedStatus : RecipeScreenEvent()
    object ChangeFavouriteStatus : RecipeScreenEvent()
    data class OpenCategoryScreen(val categoryId: Int) : RecipeScreenEvent()
    object ChangeCategories : RecipeScreenEvent()
    object DiscardCategoriesChanging : RecipeScreenEvent()
    data class ConfirmCategoriesChanging(val categories: List<Int>) : RecipeScreenEvent()
    object EditRecipe : RecipeScreenEvent()
    object DeleteRecipe : RecipeScreenEvent()

    sealed class ChangeDialogState(open val isVisible: Boolean) : RecipeScreenEvent() {
        data class Share(override val isVisible: Boolean) : ChangeDialogState(isVisible)
        data class Pictures(override val isVisible: Boolean, val selectedPicture: String = "") : ChangeDialogState(isVisible)
        data class RemoveFromRecipeBook(override val isVisible: Boolean) : ChangeDialogState(isVisible)
        data class Delete(override val isVisible: Boolean) : ChangeDialogState(isVisible)
    }

}
