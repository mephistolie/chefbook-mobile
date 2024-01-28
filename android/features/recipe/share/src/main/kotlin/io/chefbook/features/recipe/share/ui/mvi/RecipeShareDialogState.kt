package io.chefbook.features.recipe.share.ui.mvi

import android.graphics.Bitmap
import io.chefbook.libs.mvi.MviState

internal data class RecipeShareDialogState(
  val id: String? = null,
  val qr: Bitmap? = null,
  val url: String? = null,
) : MviState
