package io.chefbook.libs.exceptions

data class NotFoundException(override val message: String? = null) : Exception(message)

fun <T> notFoundResult(message: String? = null) = Result.failure<T>(NotFoundException(message))
