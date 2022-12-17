package com.mysty.chefbook.api.auth.data.remote.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.common.entities.tokens.Tokens
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TokensResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String
)

internal fun TokensResponse.toEntity(): Tokens =
    Tokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
