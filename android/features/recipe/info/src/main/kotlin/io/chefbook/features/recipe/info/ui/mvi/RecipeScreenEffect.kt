package io.chefbook.features.recipe.info.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class RecipeScreenEffect : MviSideEffect {
    data class ShowToast(val messageId: Int) : RecipeScreenEffect()
    data object ExpandSheet : RecipeScreenEffect()
    data object OpenModalBottomSheet: RecipeScreenEffect()
    data class OpenPicturesViewer(val pictures: List<String>, val startIndex: Int = 0) : RecipeScreenEffect()
    data class OpenShareDialog(val recipeId: String) : RecipeScreenEffect()
    class OpenCategoryScreen(val categoryId: String) : RecipeScreenEffect()
    data object Close : RecipeScreenEffect()
}
