package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.domain.AuthProvider
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.models.User

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthUseCases @Inject constructor(private val repository: AuthProvider) {

    fun signUp(email: String, password: String): Flow<Result<Any>> = flow {
        try {
            emit(Result.Loading)
            repository.signUp(email, password)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }

    fun signIn(email: String, password: String): Flow<Result<User>> = flow {
        try {
            emit(Result.Loading)
            repository.signInEmail(email, password)
            emit(Result.Success(null))
        } catch (e: HttpException) {
            emit(Result.Error(e, e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Result.Error(e))
        }
    }
}