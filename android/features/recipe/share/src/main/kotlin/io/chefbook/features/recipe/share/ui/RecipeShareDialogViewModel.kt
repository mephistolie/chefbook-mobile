package io.chefbook.features.recipe.share.ui

import android.content.Context
import android.graphics.Color
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.GetRecipeUseCase
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogEffect
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogIntent
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogState
import io.chefbook.ui.common.extensions.asText
import io.chefbook.core.android.qr.QRCodeWriter
import io.chefbook.core.android.R as coreR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal typealias IRecipeShareDialogViewModel = MviViewModel<RecipeShareDialogState, RecipeShareDialogIntent, RecipeShareDialogEffect>

internal class RecipeShareDialogViewModel(
  private val recipeId: String,

  private val getRecipeUseCase: GetRecipeUseCase,
  private val qrCodeWriter: QRCodeWriter,
  context: Context,
) : BaseMviViewModel<RecipeShareDialogState, RecipeShareDialogIntent, RecipeShareDialogEffect>() {

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

  private fun renderQR(
    startColor: Int,
    endColor: Int,
  ) {
    _state.update { state ->
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
        coreR.string.common_general_link_copied
      )
    )
  }

  private suspend fun copyRecipeAsText() {
    if (textRecipe == null) {
      getRecipeUseCase(recipeId = recipeId).onSuccess { recipe ->
        textRecipe = (recipe as? DecryptedRecipe)?.asText(resources)
      }
    }
    textRecipe?.let { _effect.emit(RecipeShareDialogEffect.ShareText(it)) }
  }

  private fun getRecipeLink(recipeId: String) = "https://chefbook.io/recipes/$recipeId"
}
