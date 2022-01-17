package com.cactusknights.chefbook.models

import java.io.Serializable

data class Category(
    var id: Int? = null,
    var remoteId: Int? = null,
    val name: String = "",
    val cover: String = "",
) : Serializable