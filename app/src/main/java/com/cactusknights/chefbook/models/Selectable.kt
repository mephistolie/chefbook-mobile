package com.cactusknights.chefbook.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Selectable<T>(
    var item: T? = null,
    @SerializedName("is_selected") var isSelected: Boolean = false): Serializable {

    constructor() : this(null)
}