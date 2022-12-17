package com.mysty.chefbook.api.files.data

import com.mysty.chefbook.api.common.communication.ActionStatus

internal interface IFileSource {
    suspend fun getFile(path: String): ActionStatus<ByteArray>
}
