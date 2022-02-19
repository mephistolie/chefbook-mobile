package com.cactusknights.chefbook.screens.recipeinput

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.RecipesUseCases
import com.cactusknights.chefbook.models.CookingStep
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Ingredient
import com.cactusknights.chefbook.models.Visibility
import com.cactusknights.chefbook.screens.recipeinput.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val recipeUseCases: RecipesUseCases,
) : ViewModel(), EventHandler<RecipeInputScreenEvent> {

    private val _recipeInputState: MutableStateFlow<RecipeInputScreenState> =
        MutableStateFlow(RecipeInputScreenState.Loading)
    val recipeInputState: StateFlow<RecipeInputScreenState> = _recipeInputState.asStateFlow()

    private val _viewEffect: MutableSharedFlow<RecipeInputScreenViewEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val viewEffect: SharedFlow<RecipeInputScreenViewEffect> = _viewEffect.asSharedFlow()

    private var newRecipe: DecryptedRecipe = DecryptedRecipe(
        ingredients = arrayListOf(Ingredient()),
        cooking = arrayListOf(CookingStep()),

        servings = 1,
        time = 60,
    )
    private var originalRecipe: DecryptedRecipe? = null

    private var picturesCounter = 0

    override fun obtainEvent(event: RecipeInputScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeInputScreenEvent.CreateRecipe -> _recipeInputState.emit(RecipeInputScreenState.NewRecipe(newRecipe))
                is RecipeInputScreenEvent.EditRecipe -> {
                    newRecipe = event.recipe
                    originalRecipe = event.recipe
                    _recipeInputState.emit(RecipeInputScreenState.EditRecipe(newRecipe))
                }
                is RecipeInputScreenEvent.SetVisibility -> {
                    newRecipe.visibility = event.visibility
                    emitRecipeChanges()
                }
                is RecipeInputScreenEvent.InputName -> newRecipe.name = event.name.toString()
                is RecipeInputScreenEvent.SetEncryption -> {
                    newRecipe.isEncrypted = event.encrypted
                    if (newRecipe.visibility == Visibility.PUBLIC && newRecipe.isEncrypted) {
                        newRecipe.visibility = Visibility.SHARED
                    }
                    emitRecipeChanges()
                }
                is RecipeInputScreenEvent.InputServings -> newRecipe.servings = event.servings.toString().toIntOrNull() ?: 1
                is RecipeInputScreenEvent.InputCalories -> newRecipe.calories = event.calories.toString().toIntOrNull() ?: 0
                is RecipeInputScreenEvent.InputTime -> newRecipe.time = convertInputToMinutes(event.hours.toString(), event.minutes.toString())
                is RecipeInputScreenEvent.InputDescription -> newRecipe.description = event.description
                is RecipeInputScreenEvent.ConfirmInput -> viewModelScope.launch { commitInput() }
                is RecipeInputScreenEvent.SetPreview -> {
                    val file = File(event.uri)
                    picturesCounter++
                    val compressedFile = Compressor.compress(event.context, file) {
                        default()
                        resolution(512, 512)
                        size(1048576)
                        destination(File(event.uri + "_tmp_$picturesCounter.jpg"))
                    }
                    newRecipe.preview = compressedFile.canonicalPath
                    emitRecipeChanges()
                }
                RecipeInputScreenEvent.DeletePreview -> {
                    newRecipe.preview = ""
                    emitRecipeChanges()
                }
                is RecipeInputScreenEvent.AddStepPicture -> {
                    val file = File(event.uri)
                    picturesCounter++
                    val compressedFile = Compressor.compress(event.context, file) {
                        default()
                        resolution(512, 512)
                        size(1048576)
                        destination(File(event.uri + "_tmp_$picturesCounter.jpg"))
                    }
                    try {
                        newRecipe.cooking[event.stepIndex].pictures.add(compressedFile.canonicalPath)
                    } catch (e: Exception) {}
                    emitRecipeChanges()
                }
                is RecipeInputScreenEvent.DeleteStepPicture -> {
                    try {
                        newRecipe.cooking[event.stepIndex].pictures.removeAt(event.pictureIndex)
                    } catch (e: Exception) {}
                    emitRecipeChanges()
                }
            }
        }
    }

    private suspend fun commitInput() {
        val committedRecipe = newRecipe.copy(
            ingredients = newRecipe.ingredients.filter { it.text.isNotEmpty() } as ArrayList<Ingredient>,
            cooking = newRecipe.cooking.filter { it.text.isNotEmpty() || it.pictures.isNotEmpty() } as ArrayList<CookingStep>
        )

        if (committedRecipe.name.isEmpty()) {
            _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.enter_name)); return
        }
        if (committedRecipe.ingredients.isEmpty()) {
            _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.enter_ingredients)); return
        }
        if (committedRecipe.cooking.isEmpty()) {
            _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.enter_step)); return
        }

        if (committedRecipe.remoteId == null && committedRecipe.id == null) addRecipe(committedRecipe)
        else updateRecipe(committedRecipe)
    }

    private suspend fun addRecipe(recipe: DecryptedRecipe) {
        recipeUseCases.addRecipe(recipe).collect { result ->
            when (result) {
                is Result.Loading -> _recipeInputState.emit(RecipeInputScreenState.Loading)
                is Result.Success -> _viewEffect.emit(RecipeInputScreenViewEffect.InputConfirmed(result.data!!))
                is Result.Error -> {
                    _recipeInputState.emit(RecipeInputScreenState.NewRecipe(newRecipe))
                    _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.recipe_creation_failed))
                }
            }
        }
    }

    private suspend fun updateRecipe(recipe: DecryptedRecipe) {
        recipeUseCases.updateRecipe(recipe).collect { result ->
            when (result) {
                is Result.Loading -> _recipeInputState.emit(RecipeInputScreenState.Loading)
                is Result.Success -> _viewEffect.emit(RecipeInputScreenViewEffect.InputConfirmed(recipe))
                is Result.Error -> {
                    _recipeInputState.emit(RecipeInputScreenState.EditRecipe(newRecipe))
                    _viewEffect.emit(RecipeInputScreenViewEffect.Message(R.string.recipe_update_failed))
                }
            }
        }
    }

    companion object {
        private fun convertInputToMinutes(hours: String, minutes: String): Int {
            var time = (hours.toIntOrNull() ?: 0) * 60
            time += minutes.toIntOrNull() ?: 0
            return time
        }
    }

    private suspend fun emitRecipeChanges() {
        when (recipeInputState.value) {
            is RecipeInputScreenState.NewRecipe -> _recipeInputState.emit(RecipeInputScreenState.NewRecipe(newRecipe))
            else -> _recipeInputState.emit(RecipeInputScreenState.EditRecipe(newRecipe))
        }
    }
}