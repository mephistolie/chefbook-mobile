package com.cactusknights.chefbook.domain.entities.action

data class NetworkError(
    val error: Throwable,
    override val message: String? = null
) : Exception(message)
