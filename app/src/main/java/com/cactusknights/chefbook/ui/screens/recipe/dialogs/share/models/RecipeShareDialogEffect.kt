package com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models

sealed class RecipeShareDialogEffect {
    class CopyText(val text: String, val messageId: Int) : RecipeShareDialogEffect()
    class ShareText(val text: String) : RecipeShareDialogEffect()
}