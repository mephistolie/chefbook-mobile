package com.cactusknights.chefbook.data.dto.remote.common

import com.cactusknights.chefbook.domain.entities.action.ServerError
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class ErrorResponseBody(
    @SerialName("error")
    val error: String = "",
    @SerialName("message")
    val message: String? = null,
) {

    companion object {
        fun byRawBody(body: String): ErrorResponseBody =
            try {
                Json.decodeFromString(body)
            } catch (e: SerializationException) {
                ErrorResponseBody()
            }
    }

}

fun ErrorResponseBody.toServerError() : ServerError =
    ServerError(
        type = ServerErrorTypeMapper.map(error),
        message = message
    )
