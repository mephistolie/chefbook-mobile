package com.mysty.chefbook.api.common.network.dto.responses

import androidx.annotation.Keep
import com.mysty.chefbook.api.common.communication.errors.ServerError
import com.mysty.chefbook.api.common.network.mappers.ServerErrorTypeMapper
import com.mysty.chefbook.core.constants.Strings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
internal data class ErrorResponseBody(
    @SerialName("error")
    val error: String = Strings.EMPTY,
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

internal fun ErrorResponseBody.toServerError() : ServerError =
    ServerError(
        type = ServerErrorTypeMapper.map(error),
        message = message
    )
