package com.cactusknights.chefbook.data.dto.remote.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CredentialsBody(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("user_id")
    val userId: String? = null,
)
