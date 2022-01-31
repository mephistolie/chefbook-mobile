package com.cactusknights.chefbook.models

import java.io.Serializable

open class Selectable<T>(
    var item: T? = null,
    var isSelected: Boolean = false): Serializable {
}