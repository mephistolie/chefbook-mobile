package com.cactusknights.chefbook.domain.usecases

import android.util.Log
import com.cactusknights.chefbook.domain.AuthDataSource
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.AuthRepository
import com.cactusknights.chefbook.domain.UserRepository
import com.cactusknights.chefbook.models.User

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserUseCases @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun listenToUser(): StateFlow<User?> = repository.listenToUser()

    suspend fun getUserInfo(): Flow<Result<User>> = flow {
        try {
            emit(Result.Loading)
            val user = repository.getUserInfo()
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun uploadAvatar(uri: String): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.uploadAvatar(uri)
            emit(Result.Success(null))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun deleteAvatar(): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.deleteAvatar()
            emit(Result.Success(null))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun changeName(name: String): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.changeName(name)
            emit(Result.Success(null))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}