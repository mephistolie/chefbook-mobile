package com.mysty.chefbook.features.recipe.share.ui

import android.content.Context
import android.graphics.Color
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.common.constants.Endpoints
import com.mysty.chefbook.api.recipe.domain.usecases.IGetRecipeUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.core.android.qr.QRCodeWriter
import com.mysty.chefbook.features.recipe.share.R
import com.mysty.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogEffect
import com.mysty.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogIntent
import com.mysty.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogState
import com.mysty.chefbook.ui.common.extensions.asText
import kotlinx.coroutines.flow.MutableStateFlow

internal typealias IRecipeShareDialogViewModel = IMviViewModel<RecipeShareDialogState, RecipeShareDialogIntent, RecipeShareDialogEffect>

internal class RecipeShareDialogViewModel(
  private val recipeId: String,

  private val getRecipeUseCase: IGetRecipeUseCase,
  private val qrCodeWriter: QRCodeWriter,
  context: Context,
) : MviViewModel<RecipeShareDialogState, RecipeShareDialogIntent, RecipeShareDialogEffect>() {

  private val resources = context.resources

  override val _state: MutableStateFlow<RecipeShareDialogState> =
    MutableStateFlow(RecipeShareDialogState(id = recipeId, url = getRecipeLink(recipeId)))

  var textRecipe: String? = null

  override suspend fun reduceIntent(intent: RecipeShareDialogIntent) {
    when (intent) {
      is RecipeShareDialogIntent.Close -> _effect.emit(RecipeShareDialogEffect.Close)
      is RecipeShareDialogIntent.RenderQR -> renderQR(
        startColor = intent.startColor,
        endColor = intent.endColor
      )
      is RecipeShareDialogIntent.CopyLink -> copyRecipeLink()
      is RecipeShareDialogIntent.CopyAsText -> copyRecipeAsText()
    }
  }

  private suspend fun renderQR(
    startColor: Int,
    endColor: Int,
  ) {
    updateStateSafely { state ->
      val qr = qrCodeWriter.getGradientQrCode(
        data = state.url.orEmpty(),
        backgroundColor = Color.TRANSPARENT,
        startColor = startColor,
        endColor = endColor,
      )
      state.copy(qr = qr)
    }
  }

  private suspend fun copyRecipeLink() {
    _effect.emit(
      RecipeShareDialogEffect.CopyText(
        state.value.url.orEmpty(),
        R.string.common_general_link_copied
      )
    )
  }

  private suspend fun copyRecipeAsText() {
    if (textRecipe == null) {
      getRecipeUseCase(recipeId = recipeId).collect { result ->
        if (result.isSuccess()) textRecipe = result.data().asText(resources)
      }
    }
    textRecipe?.let { _effect.emit(RecipeShareDialogEffect.ShareText(it)) }
  }

  private fun getRecipeLink(recipeId: String) = "${Endpoints.RECIPES_ENDPOINT}/$recipeId"

}
