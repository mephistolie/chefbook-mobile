package com.cactusknights.chefbook.screens.recipeinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.models.CookingStep
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.screens.recipeinput.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecipeInputViewModel @Inject constructor(
    private val recipeUseCases: RecipesUseCases
) : ViewModel(), EventHandler<RecipeInputScreenEvent> {

    private val _recipeInputState: MutableStateFlow<RecipeInputScreenState> =
        MutableStateFlow(RecipeInputScreenState.Idle)
    val recipeInputState: StateFlow<RecipeInputScreenState> = _recipeInputState.asStateFlow()

    private val _viewEffect: MutableSharedFlow<RecipeInputScreenViewEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<RecipeInputScreenViewEffect> = _viewEffect.asSharedFlow()

    private var newRecipe: RecipeInput = RecipeInput()
    private var originalRecipe: RecipeInput? = null

    override fun obtainEvent(event: RecipeInputScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeInputScreenEvent.CreateRecipe -> _recipeInputState.emit(RecipeInputScreenState.NewRecipe(newRecipe))
                is RecipeInputScreenEvent.EditRecipe -> {
                    newRecipe = event.recipe.toRecipeInput()
                    originalRecipe = event.recipe.toRecipeInput()
                    _recipeInputState.emit(RecipeInputScreenState.EditRecipe(newRecipe))
                }
                is RecipeInputScreenEvent.SetVisibility -> newRecipe.visibility = event.visibility
                is RecipeInputScreenEvent.InputName -> newRecipe.name = event.name.toString()
                is RecipeInputScreenEvent.SetEncryption -> newRecipe.encrypted = event.encrypted
                is RecipeInputScreenEvent.InputServings -> newRecipe.servings = event.servings.toString().toIntOrNull() ?: 1
                is RecipeInputScreenEvent.InputCalories -> newRecipe.calories = event.calories.toString().toIntOrNull() ?: 0
                is RecipeInputScreenEvent.InputTime -> newRecipe.time = convertInputToMinutes(event.hours.toString(), event.minutes.toString())
                is RecipeInputScreenEvent.InputDescription -> newRecipe.description = event.description
                is RecipeInputScreenEvent.ConfirmInput -> viewModelScope.launch { commitInput() }
                is RecipeInputScreenEvent.SetPreview -> {
                    val file = File(event.uri)
                    val compressedFile = Compressor.compress(event.context, file) {
                        default()
                        resolution(512, 512)
                        size(1048576)
                        destination(File(event.uri + "_test.jpg"))
                    }
                    newRecipe.preview = compressedFile.canonicalPath
                    _recipeInputState.emit(RecipeInputScreenState.NewRecipe(newRecipe))
                }
                RecipeInputScreenEvent.DeletePreview -> {
                    newRecipe.preview = ""
                    _recipeInputState.emit(RecipeInputScreenState.NewRecipe(newRecipe))
                }
            }
        }
    }

    private suspend fun commitInput() {
        val committedRecipe = newRecipe.copy()
        committedRecipe.ingredients =
            committedRecipe.ingredients.filter { it.text.isNotEmpty() } as java.util.ArrayList<Ingredient>
        committedRecipe.cooking =
            committedRecipe.cooking.filter { it.text.isNotEmpty() } as java.util.ArrayList<CookingStep>

        if (committedRecipe.name.isEmpty()) {
            _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.enter_name)); return
        }
        if (committedRecipe.ingredients.isEmpty()) {
            _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.enter_ingredients)); return
        }
        if (committedRecipe.cooking.isEmpty()) {
            _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.enter_step)); return
        }

        if (committedRecipe.remoteId == null && committedRecipe.id == null) addRecipe(committedRecipe.toRecipe())
        else updateRecipe(committedRecipe.toRecipe())
    }

    private suspend fun addRecipe(recipe: DecryptedRecipe) {
        recipeUseCases.addRecipe(recipe).collect { result ->
            if (result is Result.Success) _viewEffect.emit(
                RecipeInputScreenViewEffect.InputConfirmed(result.data!!)
            )
        }
    }

    private suspend fun updateRecipe(recipe: DecryptedRecipe) {
        recipeUseCases.updateRecipe(recipe).collect { result ->
            if (result is Result.Success) _viewEffect.emit(
                RecipeInputScreenViewEffect.InputConfirmed(recipe)
            )
        }
    }

    companion object {
        private fun convertInputToMinutes(hours: String, minutes: String): Int {
            var time = (hours.toIntOrNull() ?: 0) * 60
            time += minutes.toIntOrNull() ?: 0
            return time
        }
    }
}