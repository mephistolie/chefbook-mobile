package com.cactusknights.chefbook.screens.recipeinput

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.Selectable
import java.io.Serializable

class RecipeInputActivityState (
    val recipe: DecryptedRecipe = DecryptedRecipe(
        name = "",
        ingredients = arrayListOf(MarkdownString()),
        cooking = arrayListOf(MarkdownString())
    ),
    val categories: ArrayList<Category> = arrayListOf(),
    val selectedCategories: ArrayList<Selectable<Int>> = categories.map { Selectable(it.id) } as ArrayList<Selectable<Int>>,
    val isDone: Boolean = false,
    val message: String? = null
) : Serializable