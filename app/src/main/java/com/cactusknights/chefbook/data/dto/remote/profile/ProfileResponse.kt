package com.cactusknights.chefbook.data.dto.remote.profile

import com.cactusknights.chefbook.common.parseTimestampSafely
import com.cactusknights.chefbook.domain.entities.profile.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse constructor(

    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("username")
    val username: String? = null,
    @SerialName("creation_timestamp")
    val creationTimestamp: String,
    @SerialName("avatar")
    val avatar: String? = null,
    @SerialName("premium")
    val premium: Boolean = false,
    @SerialName("broccoins")
    val broccoins: Int = 0,
) 

fun ProfileResponse.toEntity(): Profile {
    return Profile(
        id = id,
        email = email,
        username = username,
        creationDate = parseTimestampSafely(creationTimestamp),
        premium = premium,
        avatar = avatar,
        broccoins = broccoins,
        isOnline = true,
    )
}
