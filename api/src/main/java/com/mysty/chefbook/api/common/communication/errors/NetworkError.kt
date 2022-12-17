package com.mysty.chefbook.api.common.communication.errors

data class NetworkError(
    val error: Throwable,
    override val message: String? = null
) : Exception(message)
