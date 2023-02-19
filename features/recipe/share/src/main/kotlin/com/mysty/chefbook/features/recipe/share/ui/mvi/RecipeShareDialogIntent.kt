package com.mysty.chefbook.features.recipe.share.ui.mvi

import android.graphics.Color
import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class RecipeShareDialogIntent : MviIntent {
    object Close : RecipeShareDialogIntent()

    data class RenderQR(
        val startColor: Int = Color.BLACK,
        val endColor: Int = startColor,
    ) : RecipeShareDialogIntent()
    object CopyLink : RecipeShareDialogIntent()
    object CopyAsText : RecipeShareDialogIntent()
}