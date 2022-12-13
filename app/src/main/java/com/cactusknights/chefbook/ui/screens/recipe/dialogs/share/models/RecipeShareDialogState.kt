package com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models

import android.graphics.Bitmap

data class RecipeShareDialogState(
    val id: String? = null,
    val qr: Bitmap? = null,
    val url: String? = null,
)