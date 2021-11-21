package com.cactusknights.chefbook.models

import java.io.Serializable
import java.sql.Timestamp

data class User constructor(

    var id: Int = 0,
    var email: String = "",
    var name: String = "",
    var premium: Timestamp? = null

) : Serializable