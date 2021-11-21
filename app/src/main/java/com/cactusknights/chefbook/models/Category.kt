package com.cactusknights.chefbook.models

import java.io.Serializable

data class Category(
    var id: Int = 0,
    var name: String = "",
    var type: Int = 0,
) : Serializable