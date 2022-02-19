package com.cactusknights.chefbook.screens.recipeinput.models

import com.cactusknights.chefbook.models.DecryptedRecipe

sealed class RecipeInputScreenState {
    object Loading : RecipeInputScreenState()
    class NewRecipe(val recipeInput: DecryptedRecipe) : RecipeInputScreenState()
    class EditRecipe(val recipeInput: DecryptedRecipe) : RecipeInputScreenState()
}