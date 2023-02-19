package com.mysty.chefbook.api.files.domain.usecases

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.files.data.repositories.IFilesRepo

interface ICompressImageUseCase {
    suspend operator fun invoke(
        path: String,
        width: Int = 1284,
        height: Int = 1284,
        maxFileSize: Long = 1048576,
        quality: Int = 100,
    ): ActionStatus<String>
}

internal class CompressImageUseCase(
    private val repo: IFilesRepo,
) : ICompressImageUseCase {

    override suspend operator fun invoke(
        path: String,
        width: Int,
        height: Int,
        maxFileSize: Long,
        quality: Int,
    ): ActionStatus<String> = repo.compressImage(
        path = path,
        width = width,
        height = height,
        maxFileSize = maxFileSize,
        quality = quality,
    )

}
