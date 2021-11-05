package com.cactusknights.chefbook.source.remote.dto

import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.Selectable
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class RecipeDto (
    var id: String = "",
    var name: String = "Recipe",
    @SerializedName("owner_id") var ownerId: Int,
    var isFavourite : Boolean = false,

    var servings : Int = 1,
    var time : Int = 15,
    var calories : Int = 0,
    var categories : ArrayList<String> = arrayListOf(),

    var ingredients : ArrayList<Selectable<String>> = arrayListOf(),
    var cooking : ArrayList<Selectable<String>> = arrayListOf(),

    var visibility : String = "",
    @SerializedName("creation_timestamp") var creationTimestamp: Date = Date(),
    @SerializedName("update_timestamp") var updateTimestamp: Date = Date()
)

fun RecipeDto.toRecipe(): Recipe {
    return Recipe(
        name = name,
        isFavourite = isFavourite,
        servings = servings,
        calories = calories
    )
}