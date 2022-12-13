package com.cactusknights.chefbook.ui.screens.recipeinput.models

sealed class RecipeInputScreenEffect {
    object OnVisibilityPickerOpen: RecipeInputScreenEffect()
    object OnLanguagePickerOpen: RecipeInputScreenEffect()
    object OnEncryptionStatePickerOpen: RecipeInputScreenEffect()
    object OnEncryptedVaultMenuOpen: RecipeInputScreenEffect()
    object OnCaloriesDialogOpen: RecipeInputScreenEffect()
    data class OnIngredientDialogOpen(val ingredientId: String): RecipeInputScreenEffect()
    object OnBottomSheetClosed: RecipeInputScreenEffect()
    object OnContinue : RecipeInputScreenEffect()
    object OnBack: RecipeInputScreenEffect()
    data class OnOpenRecipe(val id: String) : RecipeInputScreenEffect()
    object OnClose : RecipeInputScreenEffect()
}
