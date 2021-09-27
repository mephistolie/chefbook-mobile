package com.cactusknights.chefbook.models.legacy

import java.io.Serializable

data class LegacyIngredient(
    var name: String = "",
    var isSection: Boolean = false): Serializable {

    constructor() : this("")
}