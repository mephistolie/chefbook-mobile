package com.mysty.chefbook.features.recipe.share.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class RecipeShareDialogEffect : MviSideEffect {
    object Close : RecipeShareDialogEffect()
    class CopyText(val text: String, val messageId: Int) : RecipeShareDialogEffect()
    class ShareText(val text: String) : RecipeShareDialogEffect()
}
