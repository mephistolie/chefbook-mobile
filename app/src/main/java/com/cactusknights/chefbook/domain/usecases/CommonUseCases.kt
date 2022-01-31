package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.common.usecases.Result
import com.cactusknights.chefbook.core.retrofit.UriDataProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CommonUseCases @Inject constructor(
    private val dataProvider: UriDataProvider
) {

    suspend fun getFileData(uri: String): Flow<Result<ByteArray>> = flow {
        try {
            emit(Result.Loading)
            val data = dataProvider.getData(uri)
            emit(Result.Success(data))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}