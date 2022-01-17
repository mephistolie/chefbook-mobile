package com.cactusknights.chefbook.screens.recipeinput.models

import android.content.Context
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Visibility

sealed class RecipeInputEvent {
    object CreateRecipe : RecipeInputEvent()
    class EditRecipe(val recipe: DecryptedRecipe) : RecipeInputEvent()

    class InputName(val name: String?) : RecipeInputEvent()
    class SetVisibility(val visibility: Visibility) : RecipeInputEvent()
    class InputServings(val servings: String?) : RecipeInputEvent()
    class InputCalories(val calories: String?) : RecipeInputEvent()
    class InputTime(val hours: String?, val minutes: String?) : RecipeInputEvent()
    class InputDescription(val description: String) : RecipeInputEvent()
    class SetPreview(val uri: String?, val context: Context) : RecipeInputEvent()
    object DeletePreview : RecipeInputEvent()

    object ConfirmInput : RecipeInputEvent()
}