package com.cactusknights.chefbook.screens.recipeinput.models

import android.content.Context
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Visibility

sealed class RecipeInputScreenEvent {
    object CreateRecipe : RecipeInputScreenEvent()
    class EditRecipe(val recipe: DecryptedRecipe) : RecipeInputScreenEvent()

    class InputName(val name: String?) : RecipeInputScreenEvent()
    class SetVisibility(val visibility: Visibility) : RecipeInputScreenEvent()
    class SetEncryption(val encrypted: Boolean) : RecipeInputScreenEvent()
    class InputServings(val servings: String?) : RecipeInputScreenEvent()
    class InputCalories(val calories: String?) : RecipeInputScreenEvent()
    class InputTime(val hours: String?, val minutes: String?) : RecipeInputScreenEvent()
    class InputDescription(val description: String) : RecipeInputScreenEvent()
    class SetPreview(val uri: String, val context: Context) : RecipeInputScreenEvent()
    object DeletePreview : RecipeInputScreenEvent()

    object ConfirmInput : RecipeInputScreenEvent()
}