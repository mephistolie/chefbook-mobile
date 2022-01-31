package com.cactusknights.chefbook.screens.recipeinput.models

import com.cactusknights.chefbook.models.Recipe

sealed class RecipeInputScreenViewEffect {
    class InputConfirmed(val committedRecipe: Recipe) : RecipeInputScreenViewEffect()
    class Message(val messageId: Int) : RecipeInputScreenViewEffect()
}