package com.cactusknights.chefbook.screens.recipe.models

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Selectable

sealed class RecipeActivityState {
    object Loading : RecipeActivityState()
    class DataUpdated(val recipe: DecryptedRecipe, val categories: List<Category>, val selectedIngredients: List<Selectable<String>>) : RecipeActivityState()
}