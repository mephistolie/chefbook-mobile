package com.cactusknights.chefbook.models

import java.io.Serializable

data class Ingredient(
    var name: String = "",
    var isSection: Boolean = false): Serializable {

    constructor() : this("")
}