package io.chefbook.features.recipe.share.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class RecipeShareDialogEffect : MviSideEffect {
  data object Close : RecipeShareDialogEffect()
  class CopyText(val text: String, val messageId: Int) : RecipeShareDialogEffect()
  class ShareText(val text: String) : RecipeShareDialogEffect()
}
