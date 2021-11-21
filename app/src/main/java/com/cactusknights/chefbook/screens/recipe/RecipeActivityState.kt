package com.cactusknights.chefbook.screens.recipe

import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Selectable
import java.io.Serializable

data class RecipeActivityState(
    val recipe: DecryptedRecipe = DecryptedRecipe(),
    var selectedIngredients: List<Selectable<String>> = listOf(),
    val isDeleted: Boolean = false,
    val isPremium: Boolean = true,
    val message: Int? = null
) : Serializable