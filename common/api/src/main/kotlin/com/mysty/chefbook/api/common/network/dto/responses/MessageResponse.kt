package com.mysty.chefbook.api.common.network.dto.responses

import androidx.annotation.Keep
import com.mysty.chefbook.core.constants.Strings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal data class MessageResponse (
    @SerialName("message")
    val message: String = Strings.EMPTY,
)
