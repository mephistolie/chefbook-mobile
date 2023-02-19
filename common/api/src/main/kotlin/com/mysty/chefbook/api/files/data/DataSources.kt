package com.mysty.chefbook.api.files.data

import com.mysty.chefbook.api.common.communication.ActionStatus

internal interface IFileSource {
    suspend fun getFile(path: String): ActionStatus<ByteArray>
}

internal interface ILocalFileSource : IFileSource {
    suspend fun compressImage(
        path: String,
        width: Int = 1284,
        height: Int = 1284,
        maxFileSize: Long = 1048576,
        quality: Int = 100,
    ): ActionStatus<String>
}
