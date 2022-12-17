package com.mysty.chefbook.api.auth.data.remote.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshTokenRequest (
    @SerialName("refresh_token")
    val refreshToken: String
)
