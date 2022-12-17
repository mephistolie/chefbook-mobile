package com.mysty.chefbook.api.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CredentialsBody(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("user_id")
    val userId: String? = null,
)
