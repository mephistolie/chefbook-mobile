package com.cactusknights.chefbook.app.models

data class Recipe constructor(
    var name: String = "",

    var servings : Int = 1,
    var time : Int = 15,
    var caloricity : Int = 0,
    var categories : MutableList<String> = mutableListOf(),
    var preview: String = "",

    var ingredients : MutableList<Ingredient> = mutableListOf(),
    var cooking : MutableList<String> = mutableListOf(),

    var favourite : Boolean = false,
    var popularity : Int = 0
) {

    constructor() : this("Pie")

    fun categories(): String {
        return categories.joinToString(separator = ", ")
    }
}