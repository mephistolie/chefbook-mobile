package com.cactusknights.chefbook.screens.recipeinput.models

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe

sealed class RecipeInputViewEffect {
    class InputConfirmed(val committedRecipe: Recipe) : RecipeInputViewEffect()
    class Message(val messageId: Int) : RecipeInputViewEffect()
}