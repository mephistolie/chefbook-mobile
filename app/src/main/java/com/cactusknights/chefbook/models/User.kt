package com.cactusknights.chefbook.models

import java.sql.Timestamp

data class User constructor(

    var id: String = "",
    var email: String = "",
    var name: String = "",
    var premium: Timestamp? = null

)