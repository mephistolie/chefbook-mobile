package com.mysty.chefbook.features.recipe.info.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class RecipeScreenEffect : MviSideEffect {
    data class ShowToast(val messageId: Int) : RecipeScreenEffect()
    object OpenBottomSheet: RecipeScreenEffect()
    data class OpenPicturesViewer(val pictures: List<String>, val startIndex: Int = 0) : RecipeScreenEffect()
    data class OpenShareDialog(val recipeId: String) : RecipeScreenEffect()
    class OpenCategoryScreen(val categoryId: String) : RecipeScreenEffect()
    object Close : RecipeScreenEffect()
}
