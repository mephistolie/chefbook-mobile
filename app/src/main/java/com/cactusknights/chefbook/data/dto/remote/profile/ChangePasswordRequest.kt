package com.cactusknights.chefbook.data.dto.remote.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    @SerialName("old_password")
    val oldPassword: String,
    @SerialName("new_password")
    val newPassword: String,
) 
