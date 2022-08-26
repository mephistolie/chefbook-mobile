package com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models

import android.content.Context
import android.content.res.Resources
import com.cactusknights.chefbook.domain.entities.recipe.Recipe

sealed class RecipeShareDialogEvent {
    class LoadShareData(val recipe: Recipe, val context: Context, val startColor: Int, val endColor: Int) : RecipeShareDialogEvent()
    object CopyLink : RecipeShareDialogEvent()
    class CopyAsText(val resources: Resources) : RecipeShareDialogEvent()
}