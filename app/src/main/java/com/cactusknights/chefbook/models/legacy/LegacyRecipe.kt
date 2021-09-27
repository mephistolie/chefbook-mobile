package com.cactusknights.chefbook.models.legacy

import com.cactusknights.chefbook.models.Selectable
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class LegacyRecipe constructor(

    var id: String = "",
    var name: String = "Recipe",
    var isFavourite : Boolean = false,
    var creationDate: Date = Date(),

    var servings : Int = 1,
    var time : String = "15 min",
    var calories : Int = 0,
    var categories : ArrayList<String> = arrayListOf(),

    var ingredients : ArrayList<LegacyIngredient> = arrayListOf(),
    var cooking : ArrayList<String> = arrayListOf(),

    ): Serializable {

    constructor() : this("")
}