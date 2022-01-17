package com.cactusknights.chefbook.screens.recipeinput.models

sealed class RecipeInputState {
    object Idle : RecipeInputState()
    class NewRecipe(val recipeInput: RecipeInput) : RecipeInputState()
    class EditRecipe(val recipeInput: RecipeInput) : RecipeInputState()
}