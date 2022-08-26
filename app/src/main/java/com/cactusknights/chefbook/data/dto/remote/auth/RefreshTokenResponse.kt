package com.cactusknights.chefbook.data.dto.remote.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse (
    @SerialName("refresh_token")
    val refreshToken: String
)
