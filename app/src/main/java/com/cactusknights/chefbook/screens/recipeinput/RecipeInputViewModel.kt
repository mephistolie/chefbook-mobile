package com.cactusknights.chefbook.screens.recipeinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityState
import com.cactusknights.chefbook.screens.recipe.models.RecipeActivityViewEffect
import com.cactusknights.chefbook.screens.recipeinput.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecipeInputViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases
) : ViewModel(), EventHandler<RecipeInputEvent> {

    private val _recipeInputState: MutableStateFlow<RecipeInputState> =
        MutableStateFlow(RecipeInputState.Idle)
    val recipeInputState: StateFlow<RecipeInputState> = _recipeInputState.asStateFlow()

    private val _viewEffect: MutableSharedFlow<RecipeInputViewEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<RecipeInputViewEffect> = _viewEffect.asSharedFlow()

    private var newRecipe: RecipeInput = RecipeInput()
    private var originalRecipe: RecipeInput? = null

    override fun obtainEvent(event: RecipeInputEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeInputEvent.CreateRecipe -> _recipeInputState.emit(RecipeInputState.NewRecipe(newRecipe))
                is RecipeInputEvent.EditRecipe -> {
                    newRecipe = event.recipe.toRecipeInput()
                    originalRecipe = event.recipe.toRecipeInput()
                    _recipeInputState.emit(RecipeInputState.EditRecipe(newRecipe))
                }
                is RecipeInputEvent.SetVisibility -> newRecipe.visibility = event.visibility
                is RecipeInputEvent.InputName -> newRecipe.name = event.name.toString()
                is RecipeInputEvent.InputServings -> newRecipe.servings = event.servings.toString().toIntOrNull() ?: 1
                is RecipeInputEvent.InputCalories -> newRecipe.calories = event.calories.toString().toIntOrNull() ?: 0
                is RecipeInputEvent.InputTime -> newRecipe.time = Utils.convertInputToMinutes(event.hours.toString(), event.minutes.toString())
                is RecipeInputEvent.InputDescription -> newRecipe.description = event.description
                is RecipeInputEvent.ConfirmInput -> viewModelScope.launch { commitInput() }
                is RecipeInputEvent.SetPreview -> {
                    val file = File(event.uri)
                    val compressedFile = Compressor.compress(event.context, file) {
                        default()
                        resolution(512, 512)
                        size(1048576)
                    }
                    newRecipe.preview = compressedFile.canonicalPath
                    _recipeInputState.emit(RecipeInputState.NewRecipe(newRecipe))
                }
                RecipeInputEvent.DeletePreview -> {
                    newRecipe.preview = ""
                    _recipeInputState.emit(RecipeInputState.NewRecipe(newRecipe))
                }
            }
        }
    }

    private suspend fun commitInput() {
        val committedRecipe = newRecipe.copy()
        committedRecipe.ingredients =
            committedRecipe.ingredients.filter { it.text.isNotEmpty() } as java.util.ArrayList<MarkdownString>
        committedRecipe.cooking =
            committedRecipe.cooking.filter { it.text.isNotEmpty() } as java.util.ArrayList<MarkdownString>

        if (committedRecipe.name.isEmpty()) {
            _viewEffect.emit(RecipeInputViewEffect.Message(R.string.enter_name)); return
        }
        if (committedRecipe.ingredients.isEmpty()) {
            _viewEffect.emit(RecipeInputViewEffect.Message(R.string.enter_ingredients)); return
        }
        if (committedRecipe.cooking.isEmpty()) {
            _viewEffect.emit(RecipeInputViewEffect.Message(R.string.enter_step)); return
        }

        if (committedRecipe.remoteId == null && committedRecipe.id == null) addRecipe(
            committedRecipe.toRecipe()
        )
        else updateRecipe(committedRecipe.toRecipe())
    }

    private suspend fun addRecipe(recipe: Recipe) {
        recipeUseCases.addRecipe(recipe).collect { result ->
            if (result is Result.Success) _viewEffect.emit(
                RecipeInputViewEffect.InputConfirmed(
                    result.data!!
                )
            )
        }
    }

    private suspend fun updateRecipe(recipe: Recipe) {
        recipeUseCases.updateRecipe(recipe).collect { result ->
            if (result is Result.Success) _viewEffect.emit(
                RecipeInputViewEffect.InputConfirmed(
                    recipe
                )
            )
        }
    }
}