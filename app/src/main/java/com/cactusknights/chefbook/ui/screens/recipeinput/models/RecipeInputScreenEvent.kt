package com.cactusknights.chefbook.ui.screens.recipeinput.models

import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.MeasureUnit
import com.cactusknights.chefbook.domain.entities.common.Visibility

sealed class RecipeInputScreenEvent {
    data class SetRecipe(val recipeId: String) : RecipeInputScreenEvent()

    data class SetName(val name: String) : RecipeInputScreenEvent()

    object OpenVisibilityPicker : RecipeInputScreenEvent()
    data class SetVisibility(val visibility: Visibility) : RecipeInputScreenEvent()
    object OpenEncryptedStatePicker : RecipeInputScreenEvent()
    data class SetEncryptedState(val isEncrypted: Boolean) : RecipeInputScreenEvent()
    object OpenLanguagePicker : RecipeInputScreenEvent()
    data class SetLanguage(val language: Language) : RecipeInputScreenEvent()
    data class SetDescription(val description: String) : RecipeInputScreenEvent()

    data class SetPreview(val uri: String) : RecipeInputScreenEvent()
    object RemovePreview : RecipeInputScreenEvent()

    data class SetServings(val servings: Int?) : RecipeInputScreenEvent()
    data class SetTime(val h: Int, val min: Int) : RecipeInputScreenEvent()
    object OpenCaloriesDialog : RecipeInputScreenEvent()
    data class SetCalories(val calories: Int?) : RecipeInputScreenEvent()
    data class SetProtein(val protein: Int?) : RecipeInputScreenEvent()
    data class SetFats(val fats: Int?) : RecipeInputScreenEvent()
    data class SetCarbohydrates(val carbs: Int?) : RecipeInputScreenEvent()

    object AddIngredient : RecipeInputScreenEvent()
    object AddIngredientSection : RecipeInputScreenEvent()
    data class OpenIngredientDialog(val ingredientId: String) : RecipeInputScreenEvent()
    data class SetIngredientItemName(val ingredientId: String, val name: String) : RecipeInputScreenEvent()
    data class SetIngredientAmount(val ingredientId: String, val amount: Int?) : RecipeInputScreenEvent()
    data class SetIngredientUnit(val ingredientId: String, val unit: MeasureUnit?) : RecipeInputScreenEvent()
    data class MoveIngredientItem(val from: Int, val to: Int) : RecipeInputScreenEvent()
    data class DeleteIngredientItem(val ingredientId: String) : RecipeInputScreenEvent()

    object AddStep : RecipeInputScreenEvent()
    object AddCookingSection : RecipeInputScreenEvent()
    data class SetCookingItemValue(val index: Int, val value: String) : RecipeInputScreenEvent()
    data class MoveStepItem(val from: Int, val to: Int) : RecipeInputScreenEvent()
    data class AddStepPicture(val stepIndex: Int, val uri: String) : RecipeInputScreenEvent()
    data class DeleteStepPicture(val stepIndex: Int, val pictureIndex: Int) : RecipeInputScreenEvent()
    data class DeleteStepItem(val index: Int) : RecipeInputScreenEvent()

    object Save : RecipeInputScreenEvent()

    data class ChangeCancelDialogState(val isVisible: Boolean) : RecipeInputScreenEvent()
    object CloseBottomSheet : RecipeInputScreenEvent()
    object Back : RecipeInputScreenEvent()
    data class OpenRecipe(val id: String) : RecipeInputScreenEvent()
    object Close : RecipeInputScreenEvent()
    object Continue : RecipeInputScreenEvent()
}