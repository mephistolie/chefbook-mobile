package com.cactusknights.chefbook.models

import com.cactusknights.chefbook.ProfileProto
import java.io.Serializable
import java.util.*

data class Profile constructor(
    val id: Int = 0,
    val email: String = "",
    val username: String = "",
    val creationDate: Date? = null,
    val avatar: String? = null,
    val premium: Date? = null,
    val isLocal : Boolean = true,
    val isOnline : Boolean = true,
    val broccoins: Int = 0
) : Serializable

fun Profile.toProto() : ProfileProto {
    return ProfileProto.newBuilder()
        .setId(id)
        .setEmail(email)
        .setUsername(username)
        .setCreationDate(creationDate?.time?:0)
        .setAvatar(avatar?:"")
        .setPremium(premium?.time?:0)
        .setBroccoins(broccoins)
        .build()
}

fun ProfileProto.toProfile() : Profile {
    return Profile(
        id = id,
        email = email,
        username = username,
        creationDate = if (creationDate > 0) Date(creationDate) else null,
        avatar = avatar.ifEmpty { null },
        premium = if (premium > 0) Date(premium) else null,
        broccoins = broccoins,
        isLocal = false,
        isOnline = false
    )
}