package com.cactusknights.chefbook.data.dto.remote.auth

import com.cactusknights.chefbook.domain.entities.common.Tokens
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokensResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String
)

fun TokensResponse.toEntity(): Tokens =
    Tokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
