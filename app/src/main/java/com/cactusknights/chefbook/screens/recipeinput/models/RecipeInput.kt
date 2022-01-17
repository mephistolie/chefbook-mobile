package com.cactusknights.chefbook.screens.recipeinput.models

import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Visibility
import java.util.*
import kotlin.collections.ArrayList

data class RecipeInput (
    var id: Int? = null,
    var remoteId: Int? = null,
    var name: String = "",
    var description : String? = null,

    var encrypted: Boolean = false,

    var ingredients: ArrayList<MarkdownString> = arrayListOf(MarkdownString()),
    var cooking: ArrayList<MarkdownString> = arrayListOf(MarkdownString()),

    var servings: Int = 1,
    var time: Int = 15,
    var calories: Int = 0,

    var preview: String? = null,

    var visibility: Visibility = Visibility.PRIVATE
)

fun RecipeInput.toRecipe() : Recipe {
    return DecryptedRecipe(
        id = id,
        remoteId = remoteId,
        name = name,
        isOwned = true,
        description = description,
        servings = servings,
        time = time,
        calories = calories,
        preview = preview,
        visibility = visibility,
        ingredients = ingredients,
        cooking = cooking
    )
}

fun DecryptedRecipe.toRecipeInput() : RecipeInput {
    return RecipeInput(
        id = id,
        remoteId = remoteId,
        name = name,
        description = description,
        servings = servings,
        time = time,
        calories = calories,
        visibility = visibility,
        ingredients = ingredients,
        preview = preview,
        cooking = cooking
    )
}