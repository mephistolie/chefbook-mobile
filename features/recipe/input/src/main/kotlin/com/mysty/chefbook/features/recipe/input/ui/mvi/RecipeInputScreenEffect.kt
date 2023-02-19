package com.mysty.chefbook.features.recipe.input.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

sealed class RecipeInputScreenEffect : MviSideEffect {

    sealed class Details : RecipeInputScreenEffect() {
        object OnVisibilityPickerOpen: Details()
        object OnLanguagePickerOpen: Details()
        object OnEncryptionStatePickerOpen: Details()
        object OnEncryptedVaultMenuOpen: Details()
        object OnCaloriesDialogOpen: Details()
    }

    sealed class Ingredients : RecipeInputScreenEffect() {
        data class OnDialogOpen(val ingredientId: String): RecipeInputScreenEffect()
    }

    object OnBack: RecipeInputScreenEffect()
    object OnBottomSheetClosed: RecipeInputScreenEffect()
    object OnContinue : RecipeInputScreenEffect()
    object OnSaved : RecipeInputScreenEffect()
    data class OnClose(val recipeId: String?) : RecipeInputScreenEffect()
}
