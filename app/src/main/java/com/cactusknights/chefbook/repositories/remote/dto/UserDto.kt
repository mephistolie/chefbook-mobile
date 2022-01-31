package com.cactusknights.chefbook.repositories.remote.dto

import com.cactusknights.chefbook.models.User
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.*

data class UserDto constructor(

    @SerializedName("user_id") val id: Int = 1,
    val email: String = "",
    val username: String = "",
    val creationDate: Date? = null,
    val avatar: String? = null,
    val premium: Timestamp? = null,
    val broccoins: Int = 0
)

fun UserDto.toUser(): User {
    return User(
        id = id,
        email = email,
        name = username,
        creationDate = creationDate,
        premium = premium,
        avatar = avatar,
        broccoins = broccoins,
        isLocal = false
    )
}

data class UsernameInputDto constructor(
    val username: String
)