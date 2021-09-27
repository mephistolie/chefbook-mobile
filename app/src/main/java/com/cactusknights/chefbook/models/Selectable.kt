package com.cactusknights.chefbook.models

import java.io.Serializable

data class Selectable<T>(
    var item: T? = null,
    var isSelected: Boolean = false): Serializable {

    constructor() : this(null)
}