package com.mysty.chefbook.features.recipe.share.ui.mvi

import android.graphics.Bitmap
import com.mysty.chefbook.core.android.mvi.MviState

internal data class RecipeShareDialogState(
    val id: String? = null,
    val qr: Bitmap? = null,
    val url: String? = null,
) : MviState
