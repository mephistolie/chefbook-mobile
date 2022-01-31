package com.cactusknights.chefbook.common.usecases

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T?) : Result<T>()
    data class Error(val exception: Exception, val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}