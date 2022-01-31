package com.cactusknights.chefbook.screens.recipeinput.models

import com.cactusknights.chefbook.models.*

data class RecipeInput (
    var id: Int? = null,
    var remoteId: Int? = null,
    var name: String = "",
    var description : String? = null,

    var encrypted: Boolean = false,

    var ingredients: ArrayList<Ingredient> = arrayListOf(Ingredient()),
    var cooking: ArrayList<CookingStep> = arrayListOf(CookingStep()),

    var servings: Int = 1,
    var time: Int = 15,
    var calories: Int = 0,

    var preview: String? = null,

    var visibility: Visibility = Visibility.PRIVATE
)

fun RecipeInput.toRecipe() : DecryptedRecipe {
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
        cooking = cooking,
        encrypted = encrypted,
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
        cooking = cooking,
        encrypted = encrypted
    )
}