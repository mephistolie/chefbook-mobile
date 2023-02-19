package com.mysty.chefbook.api.common.network.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LinkResponse (
    @SerialName("link")
    val link: String,
)
