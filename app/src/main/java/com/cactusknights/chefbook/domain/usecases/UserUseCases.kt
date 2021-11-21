package com.cactusknights.chefbook.domain.usecases

import com.cactusknights.chefbook.domain.AuthDataSource
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.AuthRepository
import com.cactusknights.chefbook.domain.UserRepository
import com.cactusknights.chefbook.models.User

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserUseCases @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun listenToUser(): Flow<User?> {
        return repository.listenToUser()
    }
}