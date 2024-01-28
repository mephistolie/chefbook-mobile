package io.chefbook.features.recipe.share.ui.mvi

import android.graphics.Color
import io.chefbook.libs.mvi.MviIntent

internal sealed class RecipeShareDialogIntent : MviIntent {
  data object Close : RecipeShareDialogIntent()

  data class RenderQR(
    val startColor: Int = Color.BLACK,
    val endColor: Int = startColor,
  ) : RecipeShareDialogIntent()

  data object CopyLink : RecipeShareDialogIntent()
  data object CopyAsText : RecipeShareDialogIntent()
}