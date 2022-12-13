package com.cactusknights.chefbook.ui.screens.recipeinput

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.Strings
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.core.coroutines.AppDispatchers
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.Loading
import com.cactusknights.chefbook.domain.entities.action.Successful
import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.MeasureUnit
import com.cactusknights.chefbook.domain.entities.common.Visibility
import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem
import com.cactusknights.chefbook.domain.entities.recipe.macronutrients.MacronutrientsInfo
import com.cactusknights.chefbook.domain.entities.recipe.toRecipeInput
import com.cactusknights.chefbook.domain.usecases.encryption.IGetEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.IObserveEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ICreateRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IGetRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IUpdateRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.settings.ISetDefaultRecipeLanguageUseCase
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEffect
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class RecipeInputScreenViewModel @Inject constructor(
    application: Application,
    private val getRecipeUseCase: IGetRecipeUseCase,
    private val createRecipeUseCase: ICreateRecipeUseCase,
    private val updateRecipeUseCase: IUpdateRecipeUseCase,
    private val setDefaultRecipeLanguageUseCase: ISetDefaultRecipeLanguageUseCase,
    private val getEncryptedVaultStateUseCase: IGetEncryptedVaultStateUseCase,
    private val observeEncryptedVaultStateUseCase: IObserveEncryptedVaultStateUseCase,
    private val dispatchers: AppDispatchers,
) : AndroidViewModel(application), EventHandler<RecipeInputScreenEvent> {

    private val _state: MutableStateFlow<RecipeInputScreenState> =
        MutableStateFlow(RecipeInputScreenState())
    val state: StateFlow<RecipeInputScreenState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<RecipeInputScreenEffect> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<RecipeInputScreenEffect> = _effect.asSharedFlow()

    private var encryptionRequested = false
    private var picturesCounter = 0

    init {
        observeEncryptedVaultUnlock()
    }

    private fun observeEncryptedVaultUnlock() {
        viewModelScope.launch {
            observeEncryptedVaultStateUseCase()
                .onEach { vaultState ->
                    if (vaultState is EncryptedVaultState.Unlocked && encryptionRequested) {
                        val currentState = state.value
                        _state.emit(state.value.copy(input = currentState.input.copy(isEncrypted = true)))
                        _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
                    }
                }
                .collect()
        }
    }

    override fun obtainEvent(event: RecipeInputScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is RecipeInputScreenEvent.SetRecipe -> setRecipe(event.recipeId)

                is RecipeInputScreenEvent.SetName -> setName(event.name)
                is RecipeInputScreenEvent.OpenVisibilityPicker -> _effect.emit(
                    RecipeInputScreenEffect.OnVisibilityPickerOpen
                )
                is RecipeInputScreenEvent.SetVisibility -> setVisibility(event.visibility)
                is RecipeInputScreenEvent.OpenEncryptedStatePicker -> _effect.emit(
                    RecipeInputScreenEffect.OnEncryptionStatePickerOpen
                )
                is RecipeInputScreenEvent.SetEncryptedState -> setEncryptedState(event.isEncrypted)
                is RecipeInputScreenEvent.OpenLanguagePicker -> _effect.emit(RecipeInputScreenEffect.OnLanguagePickerOpen)
                is RecipeInputScreenEvent.SetLanguage -> setLanguage(event.language)
                is RecipeInputScreenEvent.SetDescription -> setDescription(event.description)
                is RecipeInputScreenEvent.SetPreview -> setCompressedPreview(event.uri)
                is RecipeInputScreenEvent.RemovePreview -> removePreview()
                is RecipeInputScreenEvent.SetServings -> setServings(event.servings)
                is RecipeInputScreenEvent.SetTime -> setTime(event.h, event.min)
                is RecipeInputScreenEvent.OpenCaloriesDialog -> _effect.emit(RecipeInputScreenEffect.OnCaloriesDialogOpen)
                is RecipeInputScreenEvent.SetCalories -> setCalories(event.calories)
                is RecipeInputScreenEvent.SetProtein -> setProtein(event.protein)
                is RecipeInputScreenEvent.SetFats -> setFats(event.fats)
                is RecipeInputScreenEvent.SetCarbohydrates -> setCarbs(event.carbs)

                is RecipeInputScreenEvent.AddIngredient -> addIngredientItem(
                    IngredientItem.Ingredient(
                        id = UUID.randomUUID().toString(),
                        name = Strings.EMPTY,
                    )
                )
                is RecipeInputScreenEvent.AddIngredientSection -> addIngredientItem(
                    IngredientItem.Section(
                        id = UUID.randomUUID().toString(),
                        Strings.EMPTY
                    )
                )
                is RecipeInputScreenEvent.OpenIngredientDialog -> _effect.emit(
                    RecipeInputScreenEffect.OnIngredientDialogOpen(event.ingredientId)
                )
                is RecipeInputScreenEvent.SetIngredientItemName -> setIngredientItemName(event.ingredientId, event.name)
                is RecipeInputScreenEvent.SetIngredientAmount -> setIngredientAmount(event.ingredientId, event.amount)
                is RecipeInputScreenEvent.SetIngredientUnit -> setIngredientUnit(event.ingredientId, event.unit)
                is RecipeInputScreenEvent.MoveIngredientItem -> moveIngredientItem(event.from, event.to)
                is RecipeInputScreenEvent.DeleteIngredientItem -> deleteIngredientItem(event.ingredientId)

                is RecipeInputScreenEvent.AddStep -> addCookingItem(
                    CookingItem.Step(
                        id = UUID.randomUUID().toString(),
                        description = Strings.EMPTY
                    )
                )
                is RecipeInputScreenEvent.AddCookingSection -> addCookingItem(
                    CookingItem.Section(
                        id = UUID.randomUUID().toString(),
                        name = Strings.EMPTY
                    )
                )
                is RecipeInputScreenEvent.SetCookingItemValue -> setCookingItemValue(event.index, event.value)
                is RecipeInputScreenEvent.AddStepPicture -> addStepPicture(event.stepIndex, event.uri)
                is RecipeInputScreenEvent.DeleteStepPicture -> removeStepPicture(event.stepIndex, event.pictureIndex)
                is RecipeInputScreenEvent.MoveStepItem -> moveCookingItem(event.from, event.to)
                is RecipeInputScreenEvent.DeleteStepItem -> deleteCookingItem(event.index)

                is RecipeInputScreenEvent.Save -> saveRecipe()

                is RecipeInputScreenEvent.ChangeCancelDialogState -> _state.emit(
                    state.value.copy(
                        isCancelDialogOpen = event.isVisible
                    )
                )
                is RecipeInputScreenEvent.CloseBottomSheet -> _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
                is RecipeInputScreenEvent.Back -> _effect.emit(RecipeInputScreenEffect.OnBack)
                is RecipeInputScreenEvent.Continue -> _effect.emit(RecipeInputScreenEffect.OnContinue)
                is RecipeInputScreenEvent.OpenRecipe -> openRecipe(event.id)
                is RecipeInputScreenEvent.Close -> closeScreen()
            }
        }
    }

    private suspend fun setRecipe(recipeId: String) {
        getRecipeUseCase(recipeId)
            .onEach { result ->
                when (result) {
                    is Loading -> _state.emit(state.value.copy(isLoadingDialogOpen = true))
                    is Successful -> _state.emit(
                        RecipeInputScreenState(
                            input = result.data.toRecipeInput(),
                            recipeId = recipeId
                        )
                    )
                    is Failure -> closeScreen()
                }
            }
            .collect()
    }

    private suspend fun setName(name: String) {
        val currentState = state.value
        val input = currentState.input

        val croppedName =
            if (name.length > MAX_NAME_LENGTH) name.substring(0, MAX_NAME_LENGTH) else name

        _state.emit(currentState.copy(input = input.copy(name = croppedName)))
    }

    private suspend fun setVisibility(visibility: Visibility) {
        val currentState = state.value
        val input = currentState.input

        val isEncrypted = if (visibility == Visibility.PUBLIC) false else input.isEncrypted

        _state.emit(
            currentState.copy(
                input = input.copy(
                    visibility = visibility,
                    isEncrypted = isEncrypted
                )
            )
        )
        _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
    }

    private suspend fun setEncryptedState(isEncrypted: Boolean) {
        if (getEncryptedVaultStateUseCase() !is EncryptedVaultState.Unlocked) {
            encryptionRequested = true
            _effect.emit(RecipeInputScreenEffect.OnEncryptedVaultMenuOpen)
            return
        }

        val currentState = state.value
        val input = currentState.input

        val visibility =
            if (isEncrypted && input.visibility == Visibility.PUBLIC) Visibility.SHARED else input.visibility

        _state.emit(
            currentState.copy(
                input = input.copy(
                    visibility = visibility,
                    isEncrypted = isEncrypted
                )
            )
        )
        _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
    }

    private suspend fun setLanguage(language: Language) {
        val currentState = state.value
        val input = currentState.input

        _state.emit(currentState.copy(input = input.copy(language = language)))
        _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
        setDefaultRecipeLanguageUseCase.invoke(language)
    }

    private suspend fun setDescription(description: String) {
        val currentState = state.value
        val input = currentState.input

        val croppedDescription =
            when {
                (description.length > MAX_DESCRIPTION_LENGTH) -> description.substring(
                    0,
                    MAX_DESCRIPTION_LENGTH
                )
                (description.isNotEmpty()) -> description
                else -> null
            }

        _state.emit(currentState.copy(input = input.copy(description = croppedDescription)))
    }

    private suspend fun setCompressedPreview(uri: String) {
        val currentState = state.value
        val input = currentState.input

        val compressedPreviewPath = compressPicture(uri)
        _state.emit(currentState.copy(input = input.copy(preview = compressedPreviewPath)))
    }

    private suspend fun removePreview() {
        val currentState = state.value
        val input = currentState.input

        _state.emit(currentState.copy(input = input.copy(preview = null)))
    }

    private suspend fun setServings(count: Int?) {
        val currentState = state.value
        val input = currentState.input

        val servings = when {
            count == null -> null
            count > 99 -> 99
            count < 1 -> 1
            else -> count
        }

        _state.emit(currentState.copy(input = input.copy(servings = servings)))
    }

    private suspend fun setTime(h: Int, min: Int) {
        val currentState = state.value
        val input = currentState.input

        val time = h * 60 + min

        _state.emit(currentState.copy(input = input.copy(time = if (time > 0) time else null)))
    }

    private suspend fun setCalories(value: Int?) {
        val currentState = state.value
        val input = currentState.input

        val calories = if (value != null && value > 0) value else null

        _state.emit(currentState.copy(input = input.copy(calories = calories)))
    }

    private suspend fun setProtein(value: Int?) {
        val currentState = state.value
        val input = currentState.input

        val protein = if (value != null && value > 0) value else null
        val macronutrients =
            if (input.macronutrients != null) input.macronutrients.copy(protein = protein) else MacronutrientsInfo(
                protein = protein
            )

        filterAndEmitMacronutrients(currentState, macronutrients)
    }

    private suspend fun setFats(value: Int?) {
        val currentState = state.value
        val input = currentState.input

        val fats = if (value != null && value > 0) value else null
        val macronutrients =
            if (input.macronutrients != null) input.macronutrients.copy(fats = fats) else MacronutrientsInfo(
                fats = fats
            )

        filterAndEmitMacronutrients(currentState, macronutrients)
    }

    private suspend fun setCarbs(value: Int?) {
        val currentState = state.value
        val input = currentState.input

        val carbs = if (value != null && value > 0) value else null
        val macronutrients =
            if (input.macronutrients != null) input.macronutrients.copy(carbohydrates = carbs) else MacronutrientsInfo(
                carbohydrates = carbs
            )

        filterAndEmitMacronutrients(currentState, macronutrients)
    }

    private suspend fun filterAndEmitMacronutrients(
        currentState: RecipeInputScreenState,
        macronutrients: MacronutrientsInfo,
    ) {
        val filteredMacronutrients = getFilteredMacronutrients(macronutrients)
        _state.emit(currentState.copy(input = currentState.input.copy(macronutrients = filteredMacronutrients)))
    }

    private fun getFilteredMacronutrients(macronutrients: MacronutrientsInfo) =
        if (macronutrients.protein != null || macronutrients.fats != null || macronutrients.carbohydrates != null) macronutrients else null

    private suspend fun addIngredientItem(
        item: IngredientItem,
    ) {
        val currentState = state.value
        val input = currentState.input
        val ingredients = input.ingredients.toMutableList()
        ingredients.add(item)

        _state.emit(currentState.copy(input = input.copy(ingredients = ingredients)))
    }

    private suspend fun setIngredientItemName(
        ingredientId: String,
        name: String,
    ) {
        val currentState = state.value
        val input = currentState.input
        val ingredients = input.ingredients.toMutableList()

        _state.emit(currentState.copy(input = input.copy(
            ingredients = ingredients.map { item ->
                if (item.id != ingredientId) {
                    item
                } else {
                    when (item) {
                        is IngredientItem.Section -> item.copy(
                            name = name.substring(
                                0,
                                min(name.length, MAX_NAME_LENGTH)
                            )
                        )
                        is IngredientItem.Ingredient -> item.copy(
                            name = name.substring(
                                0,
                                min(name.length, MAX_NAME_LENGTH)
                            )
                        )
                        else -> item
                    }
                }
            }
        )))
    }

    private suspend fun setIngredientAmount(
        ingredientId: String,
        amount: Int?,
    ) {
        val currentState = state.value
        val input = currentState.input
        val ingredients = input.ingredients.toMutableList()

        _state.emit(currentState.copy(input = input.copy(
            ingredients = ingredients.map { item ->
                if (item.id == ingredientId && item is IngredientItem.Ingredient) {
                    item.copy(amount = amount)
                } else {
                    item
                }
            }
        )))
    }

    private suspend fun setIngredientUnit(
        ingredientId: String,
        unit: MeasureUnit?,
    ) {
        val currentState = state.value
        val input = currentState.input
        val ingredients = input.ingredients.toMutableList()

        _state.emit(currentState.copy(input = input.copy(
            ingredients = ingredients.map { item ->
                if (item.id == ingredientId && item is IngredientItem.Ingredient) {
                    item.copy(unit = unit)
                } else {
                    item
                }
            }
        )))
    }

    private suspend fun moveIngredientItem(
        from: Int,
        to: Int,
    ) {
        val currentState = state.value
        val input = currentState.input

        if (input.ingredients.size > max(from, to)) {
            _state.emit(currentState.copy(input = input.copy(
                ingredients = input.ingredients.toMutableList().apply {
                    add(to, removeAt(from))
                }
            )))
        }
    }

    private suspend fun deleteIngredientItem(
        ingredientId: String,
    ) {
        val currentState = state.value
        val input = currentState.input

        _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
        _state.emit(currentState.copy(input = input.copy(
            ingredients = input.ingredients.filter { ingredient -> ingredient.id != ingredientId }
        )))
    }

    private suspend fun addCookingItem(
        item: CookingItem,
    ) {
        val currentState = state.value
        val input = currentState.input
        val cooking = input.cooking.toMutableList()
        cooking.add(item)

        _state.emit(currentState.copy(input = input.copy(cooking = cooking)))
    }

    private suspend fun setCookingItemValue(
        itemIndex: Int,
        value: String,
    ) {
        val currentState = state.value
        val input = currentState.input
        val cooking = input.cooking.toMutableList()

        _state.emit(currentState.copy(input = input.copy(
            cooking = cooking.mapIndexed { index, item ->
                if (index != itemIndex) {
                    item
                } else {
                    when (item) {
                        is CookingItem.Section -> item.copy(
                            name = value.substring(
                                0,
                                min(value.length, MAX_NAME_LENGTH)
                            )
                        )
                        is CookingItem.Step -> item.copy(
                            description = value.substring(
                                0,
                                min(value.length, MAX_STEP_LENGTH)
                            )
                        )
                        else -> item
                    }
                }
            }
        )))
    }

    private suspend fun addStepPicture(
        stepIndex: Int,
        uri: String
    ) {
        val compressedPicturePath = compressPicture(uri)

        val currentState = state.value
        val input = currentState.input
        val step = (input.cooking.getOrNull(stepIndex) as? CookingItem.Step) ?: return
        val pictures = (step.pictures?.toMutableList() ?: mutableListOf())
        pictures.add(compressedPicturePath)
        val updatedStep = step.copy(pictures = pictures)

        _state.emit(currentState.copy(input = input.copy(
            cooking = input.cooking.mapIndexed { index, item ->
                if (index != stepIndex) item else updatedStep
            }
        )))
    }

    private suspend fun removeStepPicture(
        stepIndex: Int,
        pictureIndex: Int,
    ) {
        val currentState = state.value
        val input = currentState.input

        _state.emit(currentState.copy(input = input.copy(
            cooking = input.cooking.mapIndexed { index, item ->
                if (index != stepIndex || item !is CookingItem.Step)
                    item
                else {
                    val pictures = item.pictures?.filterIndexed { i, _ -> i != pictureIndex }
                    item.copy(pictures = pictures?.ifEmpty { null })
                }
            }
        )))
    }

    private suspend fun moveCookingItem(
        from: Int,
        to: Int,
    ) {
        val currentState = state.value
        val input = currentState.input

        if (input.cooking.size > max(from, to)) {
            _state.emit(currentState.copy(input = input.copy(
                cooking = input.cooking.toMutableList().apply {
                    add(to, removeAt(from))
                }
            )))
        }
    }

    private suspend fun deleteCookingItem(
        itemIndex: Int,
    ) {
        val currentState = state.value
        val input = currentState.input

        _effect.emit(RecipeInputScreenEffect.OnBottomSheetClosed)
        _state.emit(currentState.copy(input = input.copy(
            cooking = input.cooking.filterIndexed { index, _ -> index != itemIndex }
        )))
    }

    private suspend fun saveRecipe() {
        val currentState = state.value

        if (currentState.recipeId == null) {
            createRecipeUseCase(currentState.input)
                .onEach { result ->
                    _state.emit(
                        when (result) {
                            is Loading -> currentState.copy(isLoadingDialogOpen = true)
                            is Successful -> currentState.copy(
                                recipeId = result.data.id,
                                isRecipeSavedDialogOpen = true
                            )
                            is Failure -> currentState
                        }
                    )
                }
                .collect()
        } else {
            updateRecipeUseCase(currentState.recipeId, currentState.input)
                .onEach { result ->
                    when (result) {
                        is Loading -> _state.emit(currentState.copy(isLoadingDialogOpen = true))
                        is Successful -> openRecipe(result.data.id)
                        is Failure -> _state.emit(currentState)
                    }
                }
                .collect()
        }
    }

    private suspend fun openRecipe(recipeId: String) {
        _state.emit(state.value.copy(isRecipeSavedDialogOpen = false))
        _effect.emit(RecipeInputScreenEffect.OnOpenRecipe(recipeId))
    }

    private suspend fun closeScreen() {
        _state.emit(state.value.copy(isRecipeSavedDialogOpen = false))
        _effect.emit(RecipeInputScreenEffect.OnClose)
    }

    private suspend fun compressPicture(
        path: String,
    ): String = withContext(dispatchers.default) {
        val file = File(path)
        picturesCounter++
        val compressedFile = Compressor.compress(getApplication<Application>().applicationContext, file) {
            resolution(1284, 1284)
            size(1048576)
            quality(100)
            destination(File(path + "_tmp_$picturesCounter.jpg"))
        }
        return@withContext compressedFile.canonicalPath
    }

    companion object {
        private const val MAX_NAME_LENGTH = 100
        private const val MAX_DESCRIPTION_LENGTH = 1500
        private const val MAX_STEP_LENGTH = 6000
    }
}
