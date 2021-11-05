package com.cactusknights.chefbook.source.remote.dto

import com.cactusknights.chefbook.models.User
import java.sql.Timestamp

data class UserDto constructor(

    var id: String = "",
    var email: String = "",
    var name: String = "",
    var premium: Timestamp? = null

)

fun UserDto.toUser(): User {
    return User(
        id = id,
        email = email,
        name = name,
        premium = premium
    )
}