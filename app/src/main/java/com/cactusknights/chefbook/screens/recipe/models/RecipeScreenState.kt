package com.cactusknights.chefbook.screens.recipe.models

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Selectable
import javax.crypto.SecretKey

sealed class RecipeScreenState {
    object Loading : RecipeScreenState()
    class DataLoaded(
        val recipe: DecryptedRecipe,
        val categories: List<Category>,
        val selectedIngredients: List<Selectable<String>>,
        val key: SecretKey? = null
    ) : RecipeScreenState()
    object Error : RecipeScreenState()
}