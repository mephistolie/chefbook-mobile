package com.cactusknights.chefbook.ui.screens.recipe.dialogs.share

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.core.Endpoints
import com.cactusknights.chefbook.core.qr.QRCodeWriter
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.usecases.recipe.IGetRecipeAsTextUseCase
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models.RecipeShareDialogEffect
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models.RecipeShareDialogEvent
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.models.RecipeShareDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeShareDialogViewModel @Inject constructor(
    private val getRecipeAsTextUseCase: IGetRecipeAsTextUseCase,
) : ViewModel(), EventHandler<RecipeShareDialogEvent> {

    private val _state: MutableStateFlow<RecipeShareDialogState> = MutableStateFlow(RecipeShareDialogState())
    val state: StateFlow<RecipeShareDialogState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<RecipeShareDialogEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<RecipeShareDialogEffect> = _effect.asSharedFlow()

    private var targetRecipe: Recipe? = null

    override fun obtainEvent(event: RecipeShareDialogEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeShareDialogEvent.LoadShareData -> loadData(event.recipe, event.context, event.startColor, event.endColor)
                is RecipeShareDialogEvent.CopyLink -> copyRecipeLink()
                is RecipeShareDialogEvent.CopyAsText -> copyRecipeAsText(event.resources)
            }
        }
    }

    private suspend fun loadData(recipe: Recipe, context: Context, startColor: Int, endColor: Int) {
        targetRecipe = recipe
        val link = getRecipeLink(recipe.id)
        val qr = QRCodeWriter.getGradientQrCode(
            data = link,
            context = context,
            backgroundColor = Color.TRANSPARENT,
            startColor = startColor,
            endColor = endColor,
        )
        _state.emit(RecipeShareDialogState(
            id = recipe.id,
            qr = qr,
            url = link,
        ))
    }

    private suspend fun copyRecipeLink() {
        _effect.emit(RecipeShareDialogEffect.CopyText(state.value.url.orEmpty(), R.string.common_general_link_copied))
    }

    private suspend fun copyRecipeAsText(resources: Resources) {
        targetRecipe?.let { recipe ->
            val text = getRecipeAsTextUseCase(recipe, resources)
            _effect.emit(RecipeShareDialogEffect.ShareText(text))
        }
    }

    private fun getRecipeLink(recipeId: String) = "${Endpoints.RECIPES_ENDPOINT}/$recipeId"

}
