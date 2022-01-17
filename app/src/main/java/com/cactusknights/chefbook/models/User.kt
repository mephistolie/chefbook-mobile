package com.cactusknights.chefbook.models

import java.io.Serializable
import java.sql.Timestamp
import java.util.*

data class User constructor(
    val id: Int = 0,
    val email: String = "",
    val name: String = "",
    val avatar: String? = null,
    val premium: Date? = null,
    val isLocal : Boolean = true,
    val broccoins: Int = 0
) : Serializable