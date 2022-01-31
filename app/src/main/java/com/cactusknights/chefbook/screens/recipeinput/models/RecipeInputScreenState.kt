package com.cactusknights.chefbook.screens.recipeinput.models

sealed class RecipeInputScreenState {
    object Idle : RecipeInputScreenState()
    class NewRecipe(val recipeInput: RecipeInput) : RecipeInputScreenState()
    class EditRecipe(val recipeInput: RecipeInput) : RecipeInputScreenState()
}