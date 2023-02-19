package com.mysty.chefbook.api.profile.data.remote.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.profile.domain.entities.Profile
import com.mysty.chefbook.core.parseTimestampSafely
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileResponse(
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
) {
    fun toEntity() = Profile(
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
