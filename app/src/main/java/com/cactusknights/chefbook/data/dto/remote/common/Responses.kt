package com.cactusknights.chefbook.data.dto.remote.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdResponse (
    @SerialName("id")
    val id: String,
    @SerialName("message")
    val message: String = ""
)

@Serializable
data class MessageResponse (
    @SerialName("message")
    val message: String = ""
)

@Serializable
data class LinkResponse (
    @SerialName("link")
    val link: String,
) 
