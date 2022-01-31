package com.cactusknights.chefbook.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type
import java.util.ArrayList

enum class IngredientTypes {
    INGREDIENT, SECTION
}

data class Ingredient (
    var text: String = "",
    var link: String? = null,
    val type: IngredientTypes = IngredientTypes.INGREDIENT
): Serializable

fun ArrayList<Ingredient>.toJson(): String {
    return Gson().toJson(this)
}

fun String.toIngredients(): ArrayList<Ingredient> {
    val type: Type = object : TypeToken<ArrayList<Ingredient>>() {}.type
    return Gson().fromJson(this, type)
}