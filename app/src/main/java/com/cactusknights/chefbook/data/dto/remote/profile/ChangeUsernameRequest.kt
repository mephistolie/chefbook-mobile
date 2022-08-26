package com.cactusknights.chefbook.data.dto.remote.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeUsernameRequest(
    @SerialName("username")
    val username: String
) 
